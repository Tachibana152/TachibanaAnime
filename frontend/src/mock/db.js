// =============================================================
// Mock 内存数据库：种子数据
// 数据来源：web课设素材 原静态网页（动漫信息/论坛文章）
// 仅用于后端未就绪时前端演示，后端完成后可删除 mock 层
// =============================================================

export const users = [
  { id: 1, username: 'admin', password: '123456', nickname: '管理员', role: 'SUPER_ADMIN', status: 1, createTime: '2026-01-01 10:00:00' },
  { id: 2, username: 'test', password: '123456', nickname: '测试用户', role: 'USER', status: 1, createTime: '2026-01-02 11:00:00' },
  { id: 3, username: 'tachibana', password: '123456', nickname: 'Tachibana站长', role: 'ADMIN', status: 1, createTime: '2026-01-03 09:30:00' },
  { id: 4, username: 'user01', password: '123456', nickname: '追番萌新', role: 'USER', status: 1, createTime: '2026-02-10 20:15:00' },
  { id: 5, username: 'user02', password: '123456', nickname: '老婆党', role: 'USER', status: 1, createTime: '2026-03-05 14:40:00' },
  { id: 6, username: 'user03', password: '123456', nickname: '禁止摆烂', role: 'USER', status: 0, createTime: '2026-04-18 08:22:00' },
]

export const animes = [
  // ================= 一月新番 =================
  {
    id: 1, title: '葬送的芙莉莲Ⅱ', titleJp: '葬送のフリーレン 第2期', category: 'NEW',
    cover: '/uploads/anime/Frieren2nd.jpg', background: '', original: '山田鐘人・アベツカサ（小学館）', director: '北川朋哉',
    writer: '鈴木智尋', episodes: 12, airDate: '2026年1月16日', airWeekday: '星期五', production: 'MADHOUSE',
    synopsis: '打倒了魔王的勇者一行人的后日谈——在"那之后"的故事。身为魔法使的芙莉莲是一位精灵，她和另外三人有着不一样的地方。对于生活在"之后"的世界、感受到的事情有着不一样的看法……残存世间的人们所编织的，葬送与祈祷相伴的故事—— 从"冒险的结束"开始了。',
    content: '精灵魔法使芙莉莲与勇者辛美尔一行，在打倒魔王之后各自踏上旅途。\n身为长寿种族的芙莉莲，以人类数十年的寿命为参照，重新审视那些一度被忽略的日常与羁绊。\n第二季继续讲述芙莉莲在旅途中的邂逅与告别，以及那跨越时间的情感与魔法。',
    viewCount: 2333, sort: 10, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 2, title: '中国奇谭 第二季', titleJp: '中国奇谭 第二季', category: 'NEW',
    cover: '/uploads/anime/chinese_ancient.jpg', background: '', original: '上海美术电影制片厂', director: '陈莲华、胡睿等12位导演',
    writer: '', episodes: 9, airDate: '2026年1月1日', airWeekday: '星期四', production: '上海美术电影制片厂、上影元、bilibili',
    synopsis: '动画短片集《中国奇谭2》由9部风格迥异的短片组成：《耳中人》《小雪》《拜山》《如何成为三条龙》《大鸟》《三郎》《今日动物园》《刑天》《大贵人》。12位导演在延续中式想象力的基础上，探索多元题材与风格，挖掘中式内核表达，同时增强了现实关照和寓言属性，在奇幻语境中探寻现实命题。',
    content: '2023年9月26日，哔哩哔哩宣布启动《中国奇谭》第二季，由上海美术电影制片厂、上影元、bilibili、陈廖宇工作室出品。\n作品汇集12位导演，创作9部风格迥异的短片。第二部的故事将走入烟火人间，在延续"用中国方式讲中国故事"的基础上，更侧重对人的自我身份、家庭关系、社会关系的思考和探讨。',
    viewCount: 1888, sort: 9, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 3, title: '炎炎消防队Ⅲ', titleJp: '炎炎ノ消防隊 参ノ章', category: 'NEW',
    cover: '/uploads/anime/Enen_no_Shouboutai3rd.jpg', background: '/uploads/anime/ench.webp', original: '大久保篤', director: '南川達馬',
    writer: '', episodes: 12, airDate: '2026年1月9日', airWeekday: '星期五', production: 'david production',
    synopsis: '世界各地的"人體自燃現象"使城市化为火海，特殊消防队肩负起消灭"焰人"、揭露真相的使命。第三季中，森罗日下部等人将逐步逼近传教的背后隐藏的真相。',
    content: '特殊消防队第八大队的少年队员们，在不断与焰人战斗的同时，也逐渐触及到这个世界火灾现象背后的巨大阴谋。',
    viewCount: 1450, sort: 8, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 4, title: 'Fate/strangeFake', titleJp: 'フェイト／ストレンジフェイク', category: 'NEW',
    cover: '/uploads/anime/Fate_strange_Fake.jpg', background: '/uploads/anime/FSF.webp', original: '奈須蘑菇（原作：成田良悟）', director: '榎戸骏',
    writer: '', episodes: 12, airDate: '2026年1月3日', airWeekday: '星期六', production: 'A-1 Pictures',
    synopsis: '伪圣杯战争在美利坚合众国西部城市史诺菲尔德展开。本不该存在的"假货"圣杯战争，将真正的英灵与形形色色的魔术师卷入一场混乱而危险的厮杀之中。',
    content: '成田良悟原作的群像剧，以夸张的展开与角色魅力著称，本作将正典圣杯战争的设定进一步放大，节奏激烈。',
    viewCount: 2021, sort: 7, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 5, title: '咒术回战Ⅲ', titleJp: '呪術廻戦 第3期', category: 'NEW',
    cover: '/uploads/anime/Jujutsu_Kaisen3rd.jpg', background: '/uploads/anime/juju.jpeg', original: '芥見下々', director: '朴性厚',
    writer: '', episodes: 12, airDate: '2026年1月14日', airWeekday: '星期三', production: 'MAPPA',
    synopsis: '以诅咒为食的咒术师与咒灵之间的战斗持续升级。虎杖悠仁与伙伴们在涩谷事变之后，迎来了更加残酷的展开。',
    content: '咒术师与咒灵之间暗流涌动，虎杖、伏黑、钉崎等人的命运在巨大的恶意面前不断被推向极限。',
    viewCount: 3120, sort: 6, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 6, title: '少女与战车 lovelove大作战 第2幕', titleJp: 'ガールズ&パンツァー もっとらぶらぶ作戦です! 第2幕', category: 'NEW',
    cover: '/uploads/anime/lovelove.jpg', background: '/uploads/anime/GupKV.webp', original: '「ガールズ＆パンツァー」シリーズ', director: '下田正美',
    writer: '木村暢', episodes: 1, airDate: '2026年1月30日', airWeekday: '星期五', production: 'P.A.WORKS×アクタス',
    synopsis: '因为讨厌战车道，才转学到没有战车道的大洗女子学园的西住美穗，没想到才刚刚转学过来就被学生会长叫了过去，会长要求美穗选择战车道的修行并且还要在全国大会中出场。队伍中聚集的成员全部都是超个性派——花道本家的大小姐五十铃华、热衷于恋爱的武部沙织、对战车无比狂热的秋山优花里、早上起不来的优等生冷泉麻子。',
    content: '希望与朋友们一起快乐的度过高中生活的美穗，到底能否如愿~',
    viewCount: 980, sort: 5, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 7, title: '机动战士高达 剧场版', titleJp: '機動戦士ガンダム 閃光のハサウェイ', category: 'NEW',
    cover: '/uploads/anime/MOBILE_SUIT_GUNDAM_HATHAWAY_The_Sorcery_of_Nymph_Circe.jpg', background: '/uploads/anime/700.jpg', original: '富野由悠季', director: '村濑修功',
    writer: '', episodes: 1, airDate: '2026年1月30日', airWeekday: '星期五', production: 'サンライズ',
    synopsis: '剧场版动画，讲述哈萨维·诺亚继承父亲意志，以"马夫蒂"之名向地球联邦发起革命的战斗物语。',
    content: '宇宙世纪的反抗之焰在星光中燃烧，哈萨维的决断将改写世界的未来。',
    viewCount: 1277, sort: 4, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 8, title: '我推的孩子Ⅲ', titleJp: '【推しの子】第3期', category: 'NEW',
    cover: '/uploads/anime/oshi_no_ko3rd.jpg', background: '', original: '赤坂アカ、横枪メンゴ', director: '平牧大輔',
    writer: '', episodes: 12, airDate: '2026年1月14日', airWeekday: '星期三', production: '動画工房',
    synopsis: '演艺圈与偶像的光与影。星野爱之子的复仇与成长物语，第三期将走向更加炽热的舞台。',
    content: '阿奎亚与露比各自追逐着目标，在闪耀的聚光灯下，隐藏的真相逐渐浮出水面。',
    viewCount: 2680, sort: 3, createTime: '2026-01-01 00:00:00',
  },

  // ================= 经典动画 =================
  {
    id: 11, title: 'CLANNAD', titleJp: 'CLANNAD -クラナド-', category: 'CLASSIC',
    cover: '/uploads/anime/CLANNAD.jpg', background: '/uploads/anime/CLANNAD_background.png', original: 'Key/VISUAL ARTS', director: '石原立也',
    writer: '志茂文彦', episodes: 23, airDate: '2007年10月4日', airWeekday: '星期四', production: '京都アニメーション',
synopsis: '故事发生在一个小镇上，冈崎朋也是光坂高中在校生，因为家庭原因他一直过着浑浑噩噩的生活。某天的上学途中，在樱花飞舞的坡道上，他邂逅了一名少女——古河渚，从此他的生活发生改变。渚因病休学一年，重返校园的她对周围环境感到相当不适应。她想加入戏剧部，然而戏剧部早已休部。朋也决定帮助她一起开展戏剧部的活动，二人关系变得越发亲密。',
    content: '第1话 樱花飞舞的坡道上\n在升学学校就读的高中3年生冈崎朋也，过着浑浑噩噩的每一天。直到那一天，在通往学校的坡道上，朋也遇到了一位少女……\n\n第2话 最初的一步\n朋也由于之前父亲曾经酗酒后打伤了朋也，导致其右臂不能抬太高，由此失去了打篮球的资格的事和父亲之间的长达6年的互不理解让父子间的谈话变成了愤怒的争吵……\n\n第3话 流泪之后再来一次\n淋着雨等待朋也的渚病倒了。次日，朋也为了探望她而来到渚的家……\n更多精彩内容请前往动画主页查看。',
    storyboard: '石原立也(1,4,8)、石立太一(2,8,14)、坂本一也(3,15,17,20)、荒谷朋恵(5,11)、高雄統子(6,12,18)、北之原孝將(7,13,19)、三好一郎 [ 木上益治 ](9)、武本康弘(10,16,22)、米田光良(21)、山田尚子(23)',
    performance: '石原立也(1,4)、北之原孝將(2,7,13,19)、三好一郎 [ 木上益治 ](3,9)、荒谷朋恵(5,11)、高雄統子(6,12,18)、石立太一(8,14,20)、武本康弘(10,16,22)、米田光良(15,21)、山田尚子(17,23)',
    music: '折戸伸治、戸越まごめ、麻枝准',
    charaOriginal: '樋上いたる',
    charaDesign: '池田和美',
    seriesComposition: '志茂文彦',
    artDirector: '篠原睦雄',
    colorDesign: '竹田明代',
    chiefAnimationDirector: '池田和美',
    animationDirector: '池田和美、高橋博行、植野千世子、西屋太志、高橋真梨子、池田晶子、堀口悠紀子、秋竹斉一、坂本一也',
    photographyDirector: '中上竜太',
    planning: '馬場隆博、中嶋嘉美',
    alias: 'クラナド、团子大家族',
    quote: '如果您愿意的话，让我带您去吧，这座小镇，愿望实现的地方……',
    viewCount: 8888, sort: 20, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 12, title: '悠久之翼 ~前篇~', titleJp: 'ef - a tale of memories.', category: 'CLASSIC',
    cover: '/uploads/anime/ef-the-latter-tale.jpg', background: '/uploads/anime/ef_background.jpg', original: 'minori・鏡遊・御影', director: '大沼心',
    writer: '高山カツヒコ', episodes: 12, airDate: '2007年10月6日', airWeekday: '星期六', production: 'SHAFT',
    synopsis: '这是曾经因遭受地震和大火侵袭，而一度坍塌的街道——音羽。而这条街现在，犹如从欧洲的童话中飞出来一般，作为非常美丽的街景复苏了。就像是，将那不吉祥的灾难的记忆掩盖起来一般……在这片重生的土地上，一个童话般的恋爱物语开始了…',
    content: 'ef系列动画版分为2007年10月开始放送的 ef - a tale of memories. 与2008年10月放送的第二季 ef - a tale of melodies.。制作由大沼心监督, 新房昭之监修。着重讲述了新藤千寻和宫村宫子的主线故事。',
    viewCount: 4560, sort: 19, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 13, title: '新世纪福音战士 EVA', titleJp: '新世紀エヴァンゲリオン', category: 'CLASSIC',
    cover: '/uploads/anime/EVA.jpg', background: '/uploads/anime/EVA_b.jpeg', original: 'GAINAX → 庵野秀明', director: '庵野秀明',
    writer: '庵野秀明、榎戸洋司等', episodes: 26, airDate: '1995年10月4日', airWeekday: '星期三', production: 'タツノコプロ、GAINAX',
    synopsis: '2000年，一个科学探险队在南极洲针对被称作"第一使徒"亚当的"光之巨人"进行探险。2015年开始，一种巨型人形生物"使徒"开始在日本登陆，并向NERV总部进攻，NERV组织EVA消灭使徒。作品应用了当时颇具革命性的意识流手法，扑朔迷离、庞大复杂的故事情节，大量的神秘学、哲学、心理学概念，成为日本动画史上的一座里程碑。',
    content: '新世纪福音战士，是日本历史上少有的能带来广泛社会影响的现象级作品，被公认为日本最伟大的ACG圈钱作品之一。作品初期注重描写人物对话及战斗，随后通过情节的推进开始着重于人物内心的叙述。在TV版的后期阶段，借助强烈象征意味的画面与配音的闪现，以此表现出的精神分析性的叙述可以说是本作的标志。',
    viewCount: 9999, sort: 18, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 14, title: '命运石之门', titleJp: 'STEINS;GATE', category: 'CLASSIC',
    cover: '/uploads/anime/STEINS_GATE.jpg', background: '/uploads/anime/ST_b.jpg', original: '5pb./Nitroplus', director: '佐藤卓哉、浜崎博嗣',
    writer: '花田十輝', episodes: 25, airDate: '2011年4月6日', airWeekday: '星期三', production: 'WHITE FOX',
    synopsis: '秋叶原的"未来道具研究所"，冈部伦太郎偶然间发明了可以向过去发送邮件的"时间机器"。然而，改变过去的选择，将引向无法挽回的命运的分歧。',
    content: '游戏改编动画。时间旅行的经典之作，从第12话开始的展开令人窒息，被称为"慢热神作"。',
    viewCount: 7777, sort: 17, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 15, title: '白箱 SHIROBAKO', titleJp: 'SHIROBAKO', category: 'CLASSIC',
    cover: '/uploads/anime/SHIROBAKO.jpg', background: "/uploads/anime/shirobako_b'.jpg", original: '原创动画', director: '水岛努',
    writer: '横手美智子', episodes: 24, airDate: '2014年10月9日', airWeekday: '星期四', production: 'P.A.WORKS',
    synopsis: '以动画制作现场为舞台，讲述5位少女追逐动画梦想的故事。真实而热血地描绘了制作现场的欢笑与泪水，是献给所有动画从业者与爱好者的情书。',
    content: '立志进入动画行业的宫森葵与同伴们，在制作现场经历各种突发状况，一步步成长。业界科普与青春物语的完美结合。',
    viewCount: 6540, sort: 16, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 16, title: '猫和老鼠', titleJp: 'Tom and Jerry', category: 'CLASSIC',
    cover: '/uploads/anime/Tom_and_Jerry.jpg', background: '/uploads/anime/taj.jpg', original: 'William Hanna, Joseph Barbera', director: 'William Hanna',
    writer: '', episodes: 161, airDate: '1940年2月10日', airWeekday: '', production: 'Warner Bros.',
    synopsis: '汤姆与杰瑞这对欢喜冤家的无休止追逐，跨越了半个多世纪的经典喜剧动画，陪伴了无数人的童年。',
    content: '1940年首播，多次获得奥斯卡最佳动画短片奖，以夸张的肢体喜剧与精妙的配乐闻名于世。',
    viewCount: 12345, sort: 15, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 17, title: 'Fate/Stay Night', titleJp: 'フェイト/ステイナイト', category: 'CLASSIC',
    cover: '/uploads/anime/Fate_15th.webp', background: '/uploads/anime/Fate_15th.webp', original: '奈須蘑菇、TYPE-MOON', director: '山口祐司',
    writer: '', episodes: 24, airDate: '2006年1月6日', airWeekday: '星期五', production: 'Studio Deen',
    synopsis: '圣杯战争——七位御主与七位从者围绕可实现愿望的圣杯展开的厮杀。卫宫士郎在意外中召唤出剑士英灵Saber，被卷入这场残酷的战争。',
    content: 'TYPE-MOON同名游戏改编。王与少年，理想与现实碰撞的壮阔物语就此展开。',
    viewCount: 5210, sort: 14, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 18, title: '花开伊吕波', titleJp: '花咲くいろは', category: 'CLASSIC',
    cover: '/uploads/anime/Hanasaku_Iroha.jpg', background: '/uploads/anime/YLB.webp', original: 'P.A.WORKS', director: '安藤真裕',
    writer: '', episodes: 26, airDate: '2011年4月3日', airWeekday: '星期日', production: 'P.A.WORKS',
    synopsis: '因为母亲的突然出走，少女松前绪花来到祖母经营的温泉旅馆"喜翠庄"开始打工生活。在旅馆的忙碌日常中，绪花努力绽放属于自己的花朵。',
    content: 'P.A.WORKS的青春三部曲之一，细腻描绘了温泉旅馆中的成长与羁绊。',
    viewCount: 3390, sort: 13, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 19, title: '魔法少女小圆', titleJp: '魔法少女まどか☆マギカ', category: 'CLASSIC',
    cover: '/uploads/anime/R-C.jpg', background: '/uploads/anime/R-C.jpg', original: 'Magica Quartet', director: '新房昭之',
    writer: '虚渊玄', episodes: 12, airDate: '2011年1月7日', airWeekday: '星期五', production: 'SHAFT',
    synopsis: '温柔少女鹿目圆在神秘的转学生晓美焰与可爱生物丘比的影响下，即将成为魔法少女。然而，成为魔法少女的代价，远比想象中残酷。',
    content: '被誉为"魔法少女的成人礼"的颠覆性名作，虚渊玄剧本 × 新房昭之演出 × 梶浦由记音乐，第3话开始剧情急转直下。',
    viewCount: 8900, sort: 12, createTime: '2026-01-01 00:00:00',
  },
]

export const posts = [
  {
    id: 1, userId: 1, username: 'admin', title: '看完冰菓的感受',
    content: '冰果艺术品般的演出作画音乐，米泽老师的推理剧情我不必多说，这里说一下我个人的感受。\n\n我是考完中考，出分前每天深夜看几集慢慢看完的，特别还是在我妈同事的家里面(因为我不是走读的)。在万籁俱寂的夜晚，躺在陌生的床上，听着里面的古典音乐，欣赏京阿尼大师级别的演出，我也渐渐的进到了那个叫做神山的小镇里，沉浸在连续不断的学校日常，体会着四人的喜怒哀乐。\n\n当第22集最后几个小提琴音收尾，蔷薇色的晚霞占满屏幕，君にまつわるミステリー的前奏响起……',
    sourceUrl: 'https://bgm.tv/blog/358002', status: 1, rejectReason: '', top: 1,
    viewCount: 1520, replyCount: 3, createTime: '2026-01-05 20:30:00',
  },
  {
    id: 2, userId: 1, username: 'admin', title: '火，是检验作品质量最重要标准',
    content: '一直以来bangumi陷入了一个重大误区，火不是作品的负面buff，理应是作品荣誉的勋章。\n\n火无论在作品商业成绩还是口碑成绩都是最重要的标准，你的销量和播放量离不开火，你的评分人数和讨论度离不开火……',
    sourceUrl: 'https://bgm.tv/subject/topic/37616', status: 1, rejectReason: '', top: 1,
    viewCount: 2310, replyCount: 5, createTime: '2026-01-10 12:00:00',
  },
  {
    id: 3, userId: 2, username: 'test', title: '迟到的追番完结有感',
    content: '明明最后一集刚出就看完了，现在才有时间写哈哈。\n\n-评分9分，一集一集追下来的，中间有一集质量下滑很大（比如比目鱼），不过虽然不是每集都像第一集一样顶但是也够强了。\n第一个方面，制作水准是无可争议的，就这么优秀的制作，没有一帧画面背景不是在动的，真的太有经费了，色调看着也让人很舒服，人物动作也很流畅表情也很生动，赞的很。',
    sourceUrl: 'https://bgm.tv/blog/346005', status: 1, rejectReason: '', top: 0,
    viewCount: 860, replyCount: 2, createTime: '2026-02-01 22:15:00',
  },
  {
    id: 4, userId: 2, username: 'test', title: '求推荐一些冷门佳作（待审核示例）',
    content: '最近补番有些迷茫，想看一些人气不高但是质量很顶的作品，各位大佬有什么推荐吗？\n\n比如像《白箱》《花开伊吕波》这种不火但制作精良的。',
    sourceUrl: '', status: 0, rejectReason: '', top: 0,
    viewCount: 0, replyCount: 0, createTime: '2026-08-25 09:00:00',
  },
  {
    id: 5, userId: 4, username: 'user01', title: '一条被驳回的帖子（示例）',
    content: '这条帖子因为内容违规被管理员驳回了，示例演示用。',
    sourceUrl: '', status: 2, rejectReason: '内容与本站主题无关，请围绕动画内容发帖', top: 0,
    viewCount: 0, replyCount: 0, createTime: '2026-08-24 15:30:00',
  },
]

export const replies = [
  { id: 1, postId: 1, userId: 2, username: 'test', content: '冰菓真的值得多刷，每次看都有新感受！', createTime: '2026-01-06 08:00:00' },
  { id: 2, postId: 1, userId: 3, username: 'tachibana', content: '京阿尼的演出和米泽的推理确实是绝配。', createTime: '2026-01-06 10:20:00' },
  { id: 3, postId: 1, userId: 4, username: 'user01', content: '补番清单+1，今晚就去看。', createTime: '2026-01-07 21:40:00' },
  { id: 4, postId: 2, userId: 2, username: 'test', content: '有道理，但我觉得小火的作品质量也很高，不能一概而论。', createTime: '2026-01-11 09:00:00' },
  { id: 5, postId: 2, userId: 1, username: 'admin', content: '讨论度也是作品生命力的一部分嘛。', createTime: '2026-01-11 13:30:00' },
  { id: 6, postId: 3, userId: 1, username: 'admin', content: '这季度确实经费拉满，制作没得挑。', createTime: '2026-02-02 11:00:00' },
]

// ==================== 工具 ====================
let seq = { anime: 100, post: 100, reply: 100 }

export function nextId(kind) {
  seq[kind] += 1
  return seq[kind]
}

export function paginate(list, pageNum = 1, pageSize = 10) {
  const start = (pageNum - 1) * pageSize
  return {
    records: list.slice(start, start + pageSize),
    total: list.length,
    pageNum,
    pageSize,
  }
}

export function nowStr() {
  const d = new Date()
  const p = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())} ${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`
}

// Mock token 格式：Bearer mock-token-<username>
export function buildToken(username) {
  return `mock-token-${username}`
}

export function parseToken(token = '') {
  const t = token.replace(/^Bearer\s+/i, '')
  if (t.startsWith('mock-token-')) {
    return t.replace('mock-token-', '')
  }
  return null
}

export function ok(data = null) {
  // 与 axios 拦截器一致：直接 resolve 业务数据
  return Promise.resolve(data)
}

export function fail(code, message) {
  return Promise.reject(Object.assign(new Error(message), { code }))
}

export function findUserByToken(token = '') {
  const username = parseToken(token)
  if (!username) return null
  return users.find((u) => u.username === username && u.status === 1) || null
}