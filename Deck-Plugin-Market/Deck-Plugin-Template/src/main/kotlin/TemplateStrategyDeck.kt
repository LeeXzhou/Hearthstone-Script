import club.xiaojiawei.DeckStrategy
import club.xiaojiawei.bean.Card
import club.xiaojiawei.bean.area.DeckArea
import club.xiaojiawei.bean.area.HandArea
import club.xiaojiawei.bean.area.PlayArea
import club.xiaojiawei.bean.isValid
import club.xiaojiawei.config.log
import club.xiaojiawei.enums.CardTypeEnum
import club.xiaojiawei.enums.RunModeEnum
import club.xiaojiawei.status.WAR

/**
 * @author 李周
 * @date 2025/6/5
 */

// object Constants {
//     const val Astral_Communion = 'AT_043'   //星界沟通
//     const val Innervate = 'CORE_EX1_169'    //激活
//     const val Aquatic_Form= 'Story_11_AquaticForm'  //水栖形态
//     const val Invigorate = 'WON_014'    //鼓舞
//     const val Bennidge = 'DED_002'  //月光指引

// }

fun checkAstral(handCards: List<Card>, usableResource: Int): Boolean {
    // 检查是否有“星界沟通”卡牌
    val hasAstral = handCards.any { it.entityName == "星界沟通" }
    if (!hasAstral) return false // 如果没有“星界沟通”，直接返回false

    // 检查是否有“幸运币”或“激活”卡牌，并增加可用资源
    val additionalResources = handCards.count { it.entityName in listOf("幸运币", "激活") }
    val totalUsableResource = usableResource + additionalResources

    // 判断可用资源是否达到4或以上
    return totalUsableResource >= 4
}

fun playAstral(handCards: List<Card>, usableResource: Int) {
    val sortedHandCards = handCards.sortedBy { it.cost }

    for (handCard in sortedHandCards) {
        // 检查卡片名称是否为“幸运币”或“激活”
        if (handCard.entityName == "幸运币" || handCard.entityName == "激活") {
            handCard.action.power()
        }
        // 检查卡片名称是否为“星界沟通”
        if (handCard.entityName == "星界沟通") {
            handCard.action.power()
        }
    }
}

class TemplateStrategyDeck : DeckStrategy() {

    var hasAstral = false
    var astralCount = 0
    override fun name(): String {
//        套牌策略名
        return "星界德"
    }

    override fun getRunMode(): Array<RunModeEnum> {
//        策略允许运行的模式
        return arrayOf(RunModeEnum.WILD)
    }

    override fun deckCode(): String {
        return "AAEBAZICComLBP3EBZ/zBb6ZBuapBq3iBqD0Bqn1BqyIB/SqBwrhFa+ABK6fBK7ABOujBZqgBomhBoeoBtmxBpb0BgABA8UE/cQFo+8E/cQFqZUG/cQFAAA="
    }

    override fun id(): String {
        return "AstralDruid"
    }

    override fun executeChangeCard(cards: HashSet<Card>) {
//        TODO("执行换牌策略")
        val toList = cards.toList()
        hasAstral = false
        val keepCardNames = listOf("激活", "水栖形态", "鼓舞")
        for (card in toList) {
            if (card.entityName == "星界沟通") {
                astralCount++
                if (astralCount > 1) {
                    cards.remove(card)
                }
            } else if (card.entityName !in keepCardNames) {
                cards.remove(card)
            }
        }
    }

    /**
     * 深度复制卡牌集合
     */
    fun deepCloneCards(sourceCards: List<Card>): MutableList<Card> {
        val copyCards = mutableListOf<Card>()
        sourceCards.forEach {
            copyCards.add(it.clone())
        }
        return copyCards
    }

    /**
     * 我的回合开始时将会自动调用此方法
     */
    override fun executeOutCard() {
//        TODO("执行出牌策略")
//        需要投降时将needSurrender设为true
//        needSurrender = true
//        获取全局war
        val war = WAR
        //        我方玩家
        val me = war.me
        if (!me.isValid()) return
//        敌方玩家
        val rival = war.rival
        if (!rival.isValid()) return
//            获取战场信息

//            获取我方所有手牌
        val handCards = me.handArea.cards
//            获取我方所有场上的卡牌
        val playCards = me.playArea.cards
//            获取我方英雄
        val hero = me.playArea.hero
//            获取我方武器
        val weapon = me.playArea.weapon
//            获取我方技能
        val power = me.playArea.power
//            获取我方所有牌库中的卡牌
        val deckCards = me.deckArea.cards
//            我方当前可用水晶数
        val usableResource = me.usableResource

//            cardId是游戏写死的，每张牌的cardId都是唯一不变的，如同身份证号码，
        val heroCardId = hero?.cardId
//            entityId在每局游戏中是唯一的
        val heroEntityId = hero?.entityId
        
        if (usableResource == 6) {
            needSurrender = true
        }            

        if (!hasAstral) {
            if (checkAstral(handCards.toList(), usableResource)) {
                playAstral(handCards.toList(), usableResource)
            }else
            { 
                var AquaticCard = handCards.find { it.entityName == "水栖形态" }
                if (AquaticCard != null) {
                    AquaticCard.action.power()
                }                
            }
        }
            
        if (heroCardId == "AV_205") {
            log.info { "该卡牌为 野性之心古夫" }
        }

//            执行操作

        /*
        注意：
        1.从war中获取到的数据都是实时更新的，
        2. 当我从手牌中打出一张随从牌时，handCards会自动删除对应的卡牌（该牌动画播放完毕后才会删除），playCards则会增加对应的卡牌（如果没被反制）
        3. 建议将集合中卡牌复制到新集合中，例：playCards.toMutableList() 或 playCards.toList()
        4. 集合中Card的属性也会实时变化，如果不想变化，可以深度拷贝集合，@see deepCloneCards(List<Card>)
        */
        val copyPlayCards = playCards.toMutableList()
//            攻击

        for (playCard in copyPlayCards) {
            if (playCard.canAttack()) {
//                    判断我方随从攻击力是否大于敌方英雄血量
                if (playCard.atc >= rival.playArea.hero!!.blood()) {
//                    我方随从攻击敌方英雄
                    playCard.action.attackHero()
                } else {
                    if (rival.playArea.cards.isNotEmpty()) {
                        val rivalPlayCard = rival.playArea.cards[0]
//                            如果对方随从拥有嘲讽
                        if (rivalPlayCard.isTaunt) {
//                                我方随从攻击敌方随从
                            playCard.action.attack(rivalPlayCard)
                            playCard.clone()
                        }
                    }

                }
            }
        }

        val copyHandCards = handCards.toMutableList()
//            出牌
        for (handCard in copyHandCards) {
//                判断卡牌所在区域，当然这里的卡牌都在手牌区，仅作演示
            if (handCard.area is HandArea) {
                log.info { "该牌位于手牌区" }
            } else if (handCard.area is PlayArea) {
                log.info { "该牌位于战场区" }
            } else if (handCard.area is DeckArea) {
                log.info { "该牌位于牌库区" }
            }

            when (handCard.cardType) {
                CardTypeEnum.SPELL -> log.info { "该牌为法术" }
                CardTypeEnum.MINION -> log.info { "该牌为随从" }
                CardTypeEnum.HERO -> log.info { "该牌为英雄" }
                CardTypeEnum.HERO_POWER -> log.info { "该牌为英雄技能" }
                else -> log.info { "" }
            }
            if (handCard.cardType === CardTypeEnum.MINION) {
                log.info { "种族：" + handCard.cardRace }
                handCard.spellPower
            }

//                费用够
            if (handCard.cost <= me.usableResource) {
//                    直接打出
//                    handCard.action.power()
//                    打出到我方场上指定下标处
//                    handCard.action.power(1)
//                    打出到我方场上指定卡牌处
//                    handCard.action.power(card)
//                    如果有比较复杂的卡牌
//                    如果是阿莱克丝塔萨（生命值变15的那个）
                if (handCard.cardId == "EX1_561") {
//                        步骤：打出，指向敌方英雄（即战吼目标为敌方英雄）
                    handCard.action
                        .power()
                        ?.pointTo(rival.playArea.hero!!)
                }
            }
        }
    }

    override fun executeDiscoverChooseCard(vararg cards: Card): Int {
//        TODO("执行选择发现牌策略")
//        返回选择卡牌的下标，这里选择的第一张牌
        return 0
    }

}