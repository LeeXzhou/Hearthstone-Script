package club.xiaojiawei.hsscript.controller.javafx

import club.xiaojiawei.hsscript.utils.VersionUtil.VERSION
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.AnchorPane
import java.net.URL
import java.util.*

/**
 * @author 肖嘉威
 * @date 2023/10/14 12:43
 */
class VersionMsgController : Initializable {

    @FXML
    protected lateinit var versionDescription: TextArea

    @FXML
    protected lateinit var rootPane: AnchorPane

    @FXML
    protected lateinit var version: Label

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        version.text = VERSION
        //        TODO 版本更新时修改！！！
        versionDescription.text = """
                    🚀 新功能
                    1. 增加游戏响应超时和游戏对局超时服务
                    2. 增加自动锁屏
                    3. 允许设置游戏和战网的窗口透明度
                    4. 增加限制鼠标范围功能
                    """.trimIndent()
    }

    @FXML
    protected fun closeWindow(actionEvent: ActionEvent) {
        rootPane.scene.window.hide()
    }

}