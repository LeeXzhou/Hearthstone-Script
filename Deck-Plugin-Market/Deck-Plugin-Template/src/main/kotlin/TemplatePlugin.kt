import club.xiaojiawei.DeckPlugin

/**
 * @author 李周
 * @date 2025/6/5
 */
class TemplatePlugin: DeckPlugin {
    override fun description(): String {
//        插件的描述
        return "星界德"
    }

    override fun author(): String {
//        插件的作者
        return "Zhou"
    }

    override fun version(): String {
//        插件的版本号
        return "1.0.0-template"
    }

    override fun id(): String {
        return "AstralDruid"
    }

    override fun name(): String {
//        插件的名字
        return "星界德"
    }

    override fun homeUrl(): String {
        return "https://github.com/xjw580/Deck-Plugin-Market/tree/master/Deck-Plugin-Template"
    }
}