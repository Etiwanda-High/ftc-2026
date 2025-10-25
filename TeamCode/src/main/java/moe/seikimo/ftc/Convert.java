package moe.seikimo.ftc;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import lombok.val;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public interface Convert {
    /**
     * Convert from a SparkFun OTOS pose into a standard Pose2D.
     *
     * @param pose Pose from SparkFun OTOS.
     * @return Converted Pose2D.
     */
    static SparkFunOTOS.Pose2D convert(Pose3D pose) {
        val position = pose.getPosition();
        val orientation = pose.getOrientation();
        return new SparkFunOTOS.Pose2D(position.x, position.z, orientation.getYaw());
    }
}
