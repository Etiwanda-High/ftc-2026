package moe.seikimo.ftc;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import lombok.val;
import lombok.var;
import moe.seikimo.ftc.annotations.fields.*;
import moe.seikimo.ftc.annotations.types.*;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.game.PlayerController;
import moe.seikimo.ftc.game.prompt.PromptManager;
import moe.seikimo.ftc.robot.fsm.StateSystem;
import moe.seikimo.ftc.robot.managers.*;
import moe.seikimo.ftc.robot.Robot;
import moe.seikimo.ftc.utils.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

public interface Discoverable {
    Set<Class<?>> SYSTEMS = Set.of(
        DriveSystem.class,
        LocalizationSystem.class,
        StateSystem.class,
        IntakeSystem.class,
        LauncherSystem.class
    );

    /**
     * Discovers hardware & systems.
     */
    default void discover() {
        val robot = this.asRobot();
        val systems = robot.getSystems();

        SYSTEMS
            .stream()
            .filter(type -> type.isAnnotationPresent(RobotSystem.class))
            .sorted(Comparator.comparing(type -> {
                val annotation = type.getAnnotation(RobotSystem.class);
                Objects.requireNonNull(annotation);
                return annotation.value().ordinal();
            }))
            .forEach(type -> {
                try {
                    // Instantiate the system.
                    var system = (MonoBehaviour) this.instantiate(type);
                    systems.put(type, system);
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to instantiate system: " + type.getName(), ex);
                }
            });
    }

    /**
     * Instantiates the type given.
     *
     * @param type The type to instantiate.
     * @return The instantiated object.
     */
    default Object instantiate(Class<?> type) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        val robot = this.asRobot();

        // Get the constructor and its parameters.
        Constructor<?> constructor = type.getDeclaredConstructors()[0];
        val paramTypes = constructor.getParameterTypes();
        val params = new Object[paramTypes.length];

        // Map the parameter instances.
        for (var i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            if (paramType == HardwareMap.class) {
                params[i] = robot.hardwareMap;
            } else if (paramType == Logger.class) {
                params[i] = robot.getLogger();
            } else if (paramType.isAssignableFrom(MonoBehaviour.class)) {
                params[i] = robot.getSystems().get(paramType);
            } else if (paramType == Robot.class) {
                params[i] = robot;
            } else if (paramType == PromptManager.class) {
                params[i] = robot.getPromptManager();
            } else {
                throw new RuntimeException("Unsupported constructor parameter: " + paramType.getName());
            }
        }

        var object = constructor.newInstance(params);

        // Handle fields.
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            val fieldType = field.getType();

            if (field.isAnnotationPresent(Hardware.class)) {
                val annotation = field.getAnnotation(Hardware.class);
                Objects.requireNonNull(annotation);

                val hardwareName = annotation.value();
                val hardwareDevice = robot.hardwareMap.get(hardwareName);
                field.set(object, hardwareDevice);
            } else if (field.isAnnotationPresent(Controller.class)) {
                val annotation = field.getAnnotation(Controller.class);
                Objects.requireNonNull(annotation);

                val controllerId = annotation.value();
                if (fieldType == Gamepad.class) {
                    field.set(object, robot.getRawHandle(controllerId));
                } else if (fieldType == GamepadEx.class) {
                    field.set(object, robot.getControllerHandle(controllerId));
                } else if (fieldType == PlayerController.class) {
                    field.set(object, robot.getController(controllerId));
                } else {
                    throw new RuntimeException("Unsupported controller field type: " + fieldType.getName());
                }
            } else if (field.isAnnotationPresent(Injected.class)) {
                if (fieldType.isAssignableFrom(MonoBehaviour.class)) {
                    val systemInstance = robot.getSystems().get(fieldType);
                    Objects.requireNonNull(systemInstance);
                    field.set(object, systemInstance);
                } else if (fieldType == Robot.class) {
                    field.set(object, robot);
                } else {
                    throw new RuntimeException("Unsupported injected field type: " + fieldType.getName());
                }
            } else if (field.isAnnotationPresent(FromPrompt.class)) {
                val annotation = field.getAnnotation(FromPrompt.class);
                Objects.requireNonNull(annotation);

                val promptLabel = annotation.value();
                Enum<?> prompt = robot.getPromptManager().getValue(promptLabel);
                field.set(object, prompt);
            }
        }

        return object;
    }

    /** @return The {@link Robot}. */
    Robot asRobot();
}
