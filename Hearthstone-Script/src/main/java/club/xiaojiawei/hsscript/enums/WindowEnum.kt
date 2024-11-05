package club.xiaojiawei.hsscript.enums

import club.xiaojiawei.hsscript.consts.SCRIPT_NAME
import javafx.stage.Screen
import javafx.stage.StageStyle

/**
 * @author 肖嘉威
 * @date 2023/10/1 10:37
 */
enum class WindowEnum(
    val fxmlName: String,
    val title: String,
    val width: Double,
    val height: Double,
    val x: Double,
    val y: Double,
    val alwaysOnTop: Boolean,
    val initStyle: StageStyle
) {
    SETTINGS(
        "settings.fxml", "$SCRIPT_NAME-设置",
        600.0, 400.0, -1.0, -1.0,
        true, StageStyle.DECORATED
    ),
    MAIN(
        "main.fxml",
        SCRIPT_NAME,
        220.0,
        670.0,
        Screen.getPrimary().bounds.width - 215.0,
        (Screen.getPrimary().bounds.height - 670.0) / 2,
        true,
        StageStyle.DECORATED
    ),
    STARTUP(
        "startup.fxml", "$SCRIPT_NAME-启动页",
        558.0, 400.0, -1.0, -1.0,
        false, StageStyle.UNDECORATED
    ),
    VERSION_MSG(
        "version_msg.fxml", "版本说明",
        550.0, -1.0, -1.0, -1.0,
        true, StageStyle.DECORATED
    ),
    ;

}
