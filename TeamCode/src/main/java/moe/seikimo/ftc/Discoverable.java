package moe.seikimo.ftc;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import lombok.val;
import lombok.var;
import moe.seikimo.ftc.annotations.Controller;
import moe.seikimo.ftc.annotations.Hardware;
import moe.seikimo.ftc.annotations.RobotSystem;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.game.PlayerController;
import moe.seikimo.ftc.robot.managers.DriveSystem;
import moe.seikimo.ftc.robot.managers.LocalizationSystem;
import moe.seikimo.ftc.robot.Robot;
import moe.seikimo.ftc.utils.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

public interface Discoverable {
    Set<Class<?>> SYSTEMS = Set.of(
        DriveSystem.class,
        LocalizationSystem.class
    );

    /**
     * Discovers hardware & systems.
     */
    default void discover() {
        val robot = this.asRobot();
        val hwMap = robot.hardwareMap;
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
                    // Get the constructor and its parameters.
                    Constructor<?> constructor = type.getDeclaredConstructors()[0];
                    val paramTypes = constructor.getParameterTypes();
                    val params = new Object[paramTypes.length];

                    // Map the parameter instances.
                    for (var i = 0; i < paramTypes.length; i++) {
                        Class<?> paramType = paramTypes[i];
                        if (paramType == HardwareMap.class) {
                            params[i] = hwMap;
                        } else if (paramType == Logger.class) {
                            params[i] = robot.getLogger();
                        } else if (paramType.isAssignableFrom(MonoBehaviour.class)) {
                            params[i] = systems.get(paramType);
                        } else {
                            throw new RuntimeException("Unsupported constructor parameter: " + paramType.getName());
                        }
                    }

                    // Instantiate the system and store it.
                    val system = (MonoBehaviour) constructor.newInstance(params);
                    systems.put(type, system);

                    // Handle fields.
                    for (Field field : type.getDeclaredFields()) {
                        field.setAccessible(true);

                        if (field.isAnnotationPresent(Hardware.class)) {
                            val annotation = field.getAnnotation(Hardware.class);
                            Objects.requireNonNull(annotation);

                            val hardwareName = annotation.value();
                            val hardwareDevice = hwMap.get(hardwareName);
                            field.set(system, hardwareDevice);
                        } else if (field.isAnnotationPresent(Controller.class)) {
                            val annotation = field.getAnnotation(Controller.class);
                            Objects.requireNonNull(annotation);

                            val controllerId = annotation.value();
                            if (field.getType() == Gamepad.class) {
                                field.set(system, robot.getRawHandle(controllerId));
                            } else if (field.getType() == GamepadEx.class) {
                                field.set(system, robot.getControllerHandle(controllerId));
                            } else if (field.getType() == PlayerController.class) {
                                field.set(system, robot.getController(controllerId));
                            } else {
                                throw new RuntimeException("Unsupported controller field type: " + field.getType().getName());
                            }
                        }
                    }
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to instantiate system: " + type.getName(), ex);
                }
            });
    }

    /** @return The {@link Robot}. */
    Robot asRobot();
}
