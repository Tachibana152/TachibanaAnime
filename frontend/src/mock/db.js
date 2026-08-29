// =============================================================
// Mock 内存数据库：种子数据
// 数据来源：web课设素材 原静态网页（动漫信息/论坛文章）
// 仅用于后端未就绪时前端演示，后端完成后可删除 mock 层
// =============================================================

export const users = [
  { id: 1, username: 'admin', password: '123456', nickname: '管理员', bio: 'Tachibana 动画世界的站长，负责站点运营与用户管理。', avatar: '/uploads/avatar/default.webp', avatarPending: '', role: 'SUPER_ADMIN', status: 1, createTime: '2026-01-01 10:00:00' },
  { id: 2, username: 'test', password: '123456', nickname: '测试用户', bio: '一个热爱动画的测试账号。', avatar: '/uploads/avatar/default.webp', avatarPending: '', role: 'USER', status: 1, createTime: '2026-01-02 11:00:00' },
  { id: 3, username: 'tachibana', password: '123456', nickname: 'Tachibana站长', bio: '番组信息维护者，日常更新动漫条目。', avatar: '/uploads/avatar/default.webp', avatarPending: '', role: 'ADMIN', status: 1, createTime: '2026-01-03 09:30:00' },
  { id: 4, username: 'user01', password: '123456', nickname: '追番萌新', bio: '刚入坑的萌新，正在疯狂补番中。', avatar: '/uploads/avatar/default.webp', avatarPending: '', role: 'USER', status: 1, createTime: '2026-02-10 20:15:00' },
  { id: 5, username: 'user02', password: '123456', nickname: '老婆党', bio: '专注纸片人老婆一百年。', avatar: '/uploads/avatar/default.webp', avatarPending: '', role: 'USER', status: 1, createTime: '2026-03-05 14:40:00' },
  { id: 6, username: 'user03', password: '123456', nickname: '禁止摆烂', bio: '已禁用账号，用于演示。', avatar: '/uploads/avatar/default.webp', avatarPending: '', role: 'USER', status: 0, createTime: '2026-04-18 08:22:00' },
]

export const animes = [
  // ================= 一月新番 =================
  {
    id: 1, title: '葬送的芙莉莲Ⅱ', titleJp: '葬送のフリーレン 第2期', category: 'NEW',
    cover: '/uploads/anime/Frieren2nd.jpg', background: '', original: '山田鐘人・アベツカサ（小学館）', director: '北川朋哉',
    writer: '鈴木智尋', episodes: 12, airDate: '2026年1月16日', airWeekday: '星期五', production: 'MADHOUSE',
    music: 'Evan Call', charaDesign: '高瀬丸(高瀬亜貴子)、小嶋慶祐、藤中友里',
    seriesComposition: '鈴木智尋', artDirector: '高木佐和子', colorDesign: '大野春恵', photographyDirector: '伏原あかね',
    quote: '葬送のフリーレン',
    synopsis: '打倒了魔王的勇者一行人的后日谈——在"那之后"的故事。身为魔法使的芙莉莲是一位精灵，她和另外三人有着不一样的地方。对于生活在"之后"的世界、感受到的事情有着不一样的看法……残存世间的人们所编织的，葬送与祈祷相伴的故事—— 从"冒险的结束"开始了。',
    content: '大陆上有各种人类之外的种族存在，寿命极长的精灵族、身强力壮的矮人族、阴险狡诈的魔族和受魔法操控的不死族等。\n魔族和魔物以人类为食，使用各种魔法诱捕人类。面对魔族的侵害和魔物的袭击，人类的军队无时无刻不在与他们进行战斗。\n整个世界的大陆按照地理位置大致分为三个板块：南方大陆、中央大陆、北方大陆。中央诸国的王都位于中央大陆的东南部。\n大陆最北端的寒冷地带——"厄德(Ende)"存在着魔王城，同时也是大魔法使伏拉梅手记中记载的天堂——"灵魂安眠之地(Aureole)"的所在之地。\n通过人类和精灵族大魔法使们的不懈研究，人类社会已经能够掌握大量魔法，不过仍有部分魔法尚未被解析。作品中出现的魔法详见：魔法介绍。\n勇者辛美尔一行成功打败了魔王，而千年魔法使芙莉莲的旅行却才刚刚开始。作品中出现的重要事件详见：事件年表。',
    contributorIds: [1],
    viewCount: 2333, sort: 10, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 2, title: '中国奇谭 第二季', titleJp: '中国奇谭 第二季', category: 'NEW',
    cover: '/uploads/anime/chinese_ancient.jpg', background: '', original: '上海美术电影制片厂', director: '陈莲华、胡睿等12位导演',
    writer: '', episodes: 9, airDate: '2026年1月1日', airWeekday: '星期四', production: '上海美术电影制片厂、上影元、bilibili',
    synopsis: '动画短片集《中国奇谭2》由9部风格迥异的短片组成：《耳中人》《小雪》《拜山》《如何成为三条龙》《大鸟》《三郎》《今日动物园》《刑天》《大贵人》。12位导演在延续中式想象力的基础上，探索多元题材与风格，挖掘中式内核表达，同时增强了现实关照和寓言属性，在奇幻语境中探寻现实命题，对人的自我身份、家庭关系、社会关系等进行思考和探讨。',
    content: '2023年9月26日，哔哩哔哩副总裁张圣晏在"2023-2024 bilibili国创动画作品发布会"上，宣布启动《中国奇谭》第二季，由上海美术电影制片厂、上影元、bilibili、陈廖宇工作室出品。\n作品汇集12位导演，创作9部风格迥异的短片。第二部的故事将走入烟火人间，在延续"用中国方式讲中国故事"的基础上，更侧重对人的自我身份、家庭关系、社会关系的思考和探讨，同时继续展现本土语境下的奇幻想象力。',
    contributorIds: [1],
    viewCount: 1888, sort: 9, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 3, title: '炎炎消防队Ⅲ', titleJp: '炎炎ノ消防隊 参ノ章', category: 'NEW',
    cover: '/uploads/anime/Enen_no_Shouboutai3rd.jpg', background: '/uploads/anime/ench.webp', original: '大久保篤', director: '南川達馬',
    writer: '', episodes: 12, airDate: '2026年1月9日', airWeekday: '星期五', production: 'david production',
    music: '末廣健一郎', charaDesign: '守岡英行', seriesComposition: '亜田井',
    artDirector: '堀越由美', colorDesign: '佐藤直子', chiefAnimationDirector: '久保茉莉子、徳田夢之介、吉岡佳広', photographyDirector: '武井夏樹',
    alias: '炎炎消防队 第三季 第2部分', quote: '全人类为之恐惧——',
    synopsis: '炎が導く灼熱のダークファンタジー 最終章始動──\n「柱」を巡る激戦や〝地下(ネザー)〟調査作戦を経て、世界の大いなる秘密へと近づいたシンラたち。\n彼らの奮闘に応え、他の特殊消防隊も協力する姿勢を示し、全ての特殊消防隊は大災害を阻止すべく1つになった。\n今、世界を守る〝シンラ（ヒーロー）〟と消防官たちの最終決戦が開幕！',
    content: '没有任何异常的人类突然燃烧，化作操纵火焰的怪物"焰人"，\n并极尽破坏之能事的"人体自燃现象"。\n面对火焰的恐怖挺身而出的特殊消防队，肩负解开这一现象的谜团，并拯救人类的使命！\n由于某种理由而被人叫做"恶魔"的新队员少年·森罗，\n以"英雄"为目标，和同伴们一起，每天投身于与"焰人"的战斗当中！！',
    contributorIds: [1],
    viewCount: 1450, sort: 8, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 4, title: 'Fate/strangeFake', titleJp: 'フェイト／ストレンジフェイク', category: 'NEW',
    cover: '/uploads/anime/Fate_strange_Fake.jpg', background: '/uploads/anime/FSF.webp', original: '奈須蘑菇（原作：成田良悟）', director: '榎戸骏',
    writer: '', episodes: 12, airDate: '2026年1月3日', airWeekday: '星期六', production: 'A-1 Pictures',
    storyboard: '坂詰嵩仁、榎戸駿', performance: '榎戸駿、坂詰嵩仁', music: '澤野弘之',
    charaOriginal: '森井しづき', charaDesign: '山田有慶；副人设：滝山真哲、相音光、浜友里恵、おかざきおか',
    seriesComposition: '大東大介', artDirector: '清木亜夕', colorDesign: '茂木孝浩',
    chiefAnimationDirector: '山田有慶', animationDirector: '曽根拓人、伊藤公規、相音光、おかざきおか、近藤明也圭、普津澤時ヱ門、浜友里恵、茂木眞一',
    photographyDirector: '宮脇洋平', planning: '岩上敦宏、竹内友崇(武内崇)、佐藤真紀、羽川寛、大塚則和',
    alias: '命运-奇异赝品、FSF', quote: '聚集于此的，乃是人类史上最大的英雄谭。',
    synopsis: '魔術師〈マスター〉と英霊〈サーヴァント〉が、あらゆる願いをかなえる願望機「聖杯」をめぐり戦う――「聖杯戦争」。\nかつて、日本で行われた第五次聖杯戦争の終結から幾年。\n新たな聖杯の予兆がアメリカ合衆国西部の都市・スノーフィールドにおいて観測される。\nそして集う、魔術師〈マスター〉と英霊〈サーヴァント〉達――\n欠けたクラス。選ばれるはずのないサーヴァント。\n暗躍する国家。戦争の為に造られた街。\n――数多のイレギュラーにより、「聖杯戦争」は混迷し、捻じれていく。\n偽りの聖杯の壇上で踊る、人間と英霊〈サーヴァント〉達の饗宴が幕を開ける――。',
    content: '魔术师们为追寻实现一切心愿的愿望机「圣杯」，\n而召唤英灵借以彼此竞争的争夺战──圣杯战争。\n于日本这片土地举行的第五次圣杯战争终结后数年，\n美国西部雪原市(史诺菲尔德)出现下一场斗争。\n──那是充满虚伪的圣杯战争。\n聚集于虚伪台座的魔术师与英灵们。\n即使深知这是场虚伪的圣杯战争──他们仍旧在此之上不断舞动。\n真伪即在遥远的彼岸。\n并非为了圣杯──也非为其他任何事物，而是为贯彻他们自身的信念。\n然而就在此刻，注满容器的究竟是虚伪或真实，还是说──\nTYPE-MOON高人气视觉小说「Fate」，由成田良悟所描绘的新篇章启动！',
    contributorIds: [1],
    viewCount: 2021, sort: 7, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 5, title: '咒术回战Ⅲ', titleJp: '呪術廻戦 第3期', category: 'NEW',
    cover: '/uploads/anime/Jujutsu_Kaisen3rd.jpg', background: '/uploads/anime/juju.jpeg', original: '芥見下々', director: '朴性厚',
    writer: '', episodes: 12, airDate: '2026年1月14日', airWeekday: '星期三', production: 'MAPPA',
    storyboard: '御所園翔太', performance: '御所園翔太、高田陽介', music: '照井順政',
    charaDesign: '矢島陽介・丹羽弘美', seriesComposition: '瀬古浩司', artDirector: '東潤一', colorDesign: '松島英子',
    chiefAnimationDirector: '矢島陽介、丹羽弘美、森光恵、山崎爽太、清水貴子',
    animationDirector: '堀江由美、荏原裕子、三谷高史、野田友美、高田陽介、長友望己、奥田哲平',
    photographyDirector: '伊藤哲平', alias: 'Jujutsu Kaisen: Shimetsu Kaiyuu、呪術廻戦 死滅回游 前編',
    quote: '为了祓除诅咒，化身为诅咒的少年无法回头的壮绝物语开始启动——',
    synopsis: '2018年10月31日。\nハロウィンで賑わう渋谷駅周辺に突如"帳"が降ろされ大勢の一般人が閉じ込められる。\nそこに単独で乗り込む現代最強の呪術師・五条悟。だが、そこには五条の封印を目論む呪詛師・呪霊達が待ち構えていた。\n渋谷に集結する虎杖悠仁ら、数多くの呪術師たち。\nかつてない大規模な呪い合い「渋谷事変」が始まる―。\nそして戦いは、史上最悪の術師・加茂憲倫が仕組んだ殺し合い「死滅回游」へ。\n「渋谷事変」を経て、魔窟と化す全国10の結界コロニー。\nそんな大混乱の最中、虎杖の死刑執行役として特級術師・乙骨憂太が立ちはだかる。\n絶望の中で、なおも戦い続ける虎杖。\n無情にも、刃を向ける乙骨。\n加速していく呪いの混沌。\n同じ師を持つ虎杖と乙骨、二人の死闘が始まる——',
    content: '虎杖悠仁是一位体育万能的高中生，\n某天他为了从「咒物」危机中解救学长和学姐，\n而吞下了诅咒的手指，让「两面宿傩」这种诅咒跟自己合而为一。\n之后他加入了专门培养咒术师的学校「咒术高专」，\n并遇到了伏黑惠与钉崎野蔷薇这两位同学。\n某日，突然出现的「特级咒物」，\n他们三人就奉命到现场支援。\n为了实现爷爷要他「助人」的遗言，\n虎杖将会继续与「诅咒」奋斗下去！',
    contributorIds: [1],
    viewCount: 3120, sort: 6, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 6, title: '少女与战车 lovelove大作战 第2幕', titleJp: 'ガールズ&パンツァー もっとらぶらぶ作戦です! 第2幕', category: 'NEW',
    cover: '/uploads/anime/lovelove.jpg', background: '/uploads/anime/GupKV.webp', original: '「ガールズ＆パンツァー」シリーズ', director: '下田正美',
    writer: '木村暢', episodes: 1, airDate: '2026年1月30日', airWeekday: '星期五', production: 'P.A.WORKS×アクタス',
    music: '浜口史郎', charaOriginal: '島田フミカネ、杉本功；协力：野上武志', charaDesign: '杉本功', seriesComposition: '木村暢',
    quote: 'パンツァー（Panzer）フォー！！',
    synopsis: '原创动画《少女与战车》，又名《天才战车少女》《烧酒与胖次》《五个打炮的少女》《Ankou Live!》[2]，简称"GUP"，讲述的是使用战车的武技"战车道"与花道、茶道这些被并称为大和抚子的做法的世界。\n2012年5月17日宣布制作并开放官方网站，公布了首部PV及声优、制作组详情。于2012年10月8日于TOKYO MX等电视台开始播映。\n动画播出后，故事背景地茨城县大洗町成为日本又一个巡礼圣地。',
    content: '因为讨厌战车道，才转学到没有战车道的大洗女子学园的西住美穗，没想到才刚刚转学过来就被学生会长叫了过去，会长要求美穗选择战车道的修行并且还要在全国大会中出场。而且，队伍中聚集的成员全部都是超个性派——花道本家的大小姐五十铃华、热衷于恋爱的武部沙织、对战车无比狂热的秋山优花里、早上起不来的优等生冷泉麻子。\n希望与朋友们一起快乐的度过高中生活的美穗，到底能否如愿~',
    contributorIds: [1],
    viewCount: 980, sort: 5, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 7, title: '机动战士高达 剧场版', titleJp: '機動戦士ガンダム 閃光のハサウェイ', category: 'NEW',
    cover: '/uploads/anime/MOBILE_SUIT_GUNDAM_HATHAWAY_The_Sorcery_of_Nymph_Circe.jpg', background: '/uploads/anime/700.jpg', original: '富野由悠季', director: '村濑修功',
    writer: '', episodes: 1, airDate: '2026年1月30日', airWeekday: '星期五', production: 'サンライズ',
    music: '澤野弘之', charaOriginal: '美樹本晴彦', charaDesign: 'Pablo Uchida、恩田尚之、工原しげき',
    artDirector: '大久保錦一', colorDesign: 'すずきたかこ、久保木裕一', photographyDirector: '大山佳久',
    planning: 'サンライズ', alias: 'MOBILE SUIT GUNDAM HATHAWAY The Sorcery of Nymph Circe',
    quote: 'その閃光は人類の希望。那闪光的一瞬是人类的希望。',
    synopsis: '「機動戦士ガンダム」の富野由悠季監督が1989～90年に発表した全3巻の小説「機動戦士ガンダム　閃光のハサウェイ」をアニメーション映画化する3部作の第2部。「機動戦士ガンダム　逆襲のシャア」から12年後を舞台に、腐敗した地球連邦政府に反旗を翻す青年ハサウェイ・ノアの戦いを描く。\n「シャアの反乱」と呼ばれた第2次ネオ・ジオン抗争から12年後の宇宙世紀105年（U.C.0105）。圧政を強いる地球連邦政府に対し、政府高官の暗殺という方法で抵抗を開始した反地球連邦組織「マフティー」。そのリーダー、マフティー・ナビーユ・エリンの正体は、一年戦争をアムロ・レイととも戦ったブライト・ノアの息子、ハサウェイ・ノアだった。不思議な力を示す少女ギギ・アンダルシアの言葉に翻弄されながらも、マフティーとしての目的遂行のため歩みを進めるハサウェイ。一方、マフティーを追う連邦軍大佐ケネス・スレッグは、刑事警察機構のハンドリー・ヨクサンから密約を持ちかけられる。ハサウェイとケネスがそれぞれの目的のために動くなか、ギギもまた自分の役割のため、ホンコンへと旅立つ。\nハサウェイ役の小野賢章、ギギ役の上田麗奈、ケネス役の諏訪部順一らメインキャストが前作に続いて出演。監督の村瀬修功、脚本のむとうやすゆき、音楽の澤野弘之らメインスタッフも続投した。',
    content: '第二次新吉翁战争(夏亚叛乱)至今已过去了12年。\n宇宙世纪（U.C.）0105年——地球联邦政府的腐败加速了地球的污染，还采取了非人道的政策"狩猎人类"，为此成立了猎人组织，强行将没有地球居住证的平民送往宇宙。\n反地球联邦政府运动组织"马夫蒂"以暗杀这样的联邦政府高官的苛烈行为开始反抗。领导人的名字是"马夫蒂·纳比尤·艾林"。他的真实身份是在一年战争时战斗过的联邦军上校布莱德·诺亚的儿子哈萨维·诺亚。\n亲身体会过阿姆罗·雷和夏亚·阿兹纳布尔理想的他，化身为寄宿两人意志的战士，策画着借由武力抵抗开拓未来的道路。然而，与联邦军大佐凯奈斯·斯雷格和神秘的美少女琪琪·安达露西亚的邂逅极大地改变了他的命运......',
    contributorIds: [1],
    viewCount: 1277, sort: 4, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 8, title: '我推的孩子Ⅲ', titleJp: '【推しの子】第3期', category: 'NEW',
    cover: '/uploads/anime/oshi_no_ko3rd.jpg', background: '', original: '赤坂アカ、横枪メンゴ', director: '平牧大輔',
    writer: '', episodes: 12, airDate: '2026年1月14日', airWeekday: '星期三', production: '動画工房',
    music: '伊賀拓郎', charaDesign: '平山寛菜', seriesComposition: '田中仁', artDirector: '宇佐美哲也', colorDesign: '芦原明音',
    chiefAnimationDirector: '朱里、稲手遥香、錦寛乃、森田莉奈、平山寛菜、室賀彩花、水野公彰', photographyDirector: '桒野貴文',
    alias: '"Oshi no Ko" 3、Oshi no Ko 3rd Season', quote: 'この芸能界（せかい）において嘘は武器だ。在演艺圈（这个世界）中，谎言就是武器。',
    synopsis: '物語は新たなステージへ──\n『POP IN ２』のリリースから半年。ＭＥＭちょの尽力の甲斐もあり、今やB小町はブレイク寸前。\nアクアはマルチタレント、あかねは実力派女優の道を順調に歩んでいる。\n一方で、かなは以前の明るさを失っていた。\nそして、アイとゴローの死の真相を追い求め、\nルビーは芸能界を駆け上がる。\n────嘘を、武器にして。',
    content: '妇产科医生雨宫吾郎是少女偶像星野爱的"单推人"（粉丝），某天在工作中遇到了一名秘密前来问诊16岁孕妇，而她竟是星野爱本人！吾郎大受打击，但还是尊重星野爱想要秘密产子的决定，作为医生帮助着她。在星野爱腹中的双子出生之夜，吾郎被星野爱的一名"私生饭"（极端粉丝）杀害，之后竟然转生成了星野爱的儿子星野爱久爱海（阿库亚）。双子中的女孩名为星野瑠美衣（露比），是一位憧憬星野爱却遗憾因病离世的女孩的转世。兄妹俩逐渐长大，分别展现出演员和偶像的天赋，也逐渐接触到演艺圈的一些内幕。\n双子出生4年后，年满20岁的星野爱作为偶像事业有成，即将在东京巨蛋进行演出，但就在演出的前一天，她却被"私生饭"刺杀。母亲被害给双子留下了严重的心理阴影，阿库亚认定双子从未现身的亲生父亲与星野爱的死有着必然联系，就此立志找出父亲并向他复仇。\n随着时间的流逝，双子成长到了16岁，阿库亚为了在演艺圈中寻找父亲走上了成为演员的道路，露比为了实现成为偶像的梦想，兄妹俩双双进入艺校"阳东高中"就读。\n阿库亚正式开始探寻自己与星野爱的身世之谜，同时也要应对演艺圈的各种纷争和阴谋。"从现在还未有的角度切入，描写\u2018演艺圈\u2019的故事"就此展开。',
    contributorIds: [1],
    viewCount: 2680, sort: 3, createTime: '2026-01-01 00:00:00',
  },

  // ================= 经典动画 =================
  {
    id: 11, title: 'CLANNAD', titleJp: 'CLANNAD -クラナド-', category: 'CLASSIC',
    cover: '/uploads/anime/CLANNAD.jpg', background: '/uploads/anime/CLANNAD_background.png', original: 'Key/VISUAL ARTS', director: '石原立也',
    writer: '志茂文彦', episodes: 23, airDate: '2007年10月4日', airWeekday: '星期四', production: '京都アニメーション',
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
    synopsis: '故事发生在一个小镇上，冈崎朋也是光坂高中在校生，因为家庭原因他一直过着浑浑噩噩的生活。他不参加任何社团活动，唯一的朋友是春原阳平。\n某天的上学途中，在樱花飞舞的坡道上，他邂逅了一名少女——古河渚，从此他的生活发生改变。渚因病休学一年，重返校园的她对周围环境感到相当不适应。她想加入戏剧部，然而戏剧部早已休部。朋也决定帮助她一起开展戏剧部的活动，二人关系变得越发亲密。随后不久朋也在图书馆中结识了一名天才少女一之濑琴美。',
    content: '第1话 樱花飞舞的坡道上\n在升学学校就读的高中3年生冈崎朋也，过着浑浑噩噩的每一天。每天都在混吃等死做着和前一天同样的事情。世界，生活，黯淡无光。直到那一天，在通往学校的坡道上，朋也遇到了一位少女……\n\n第2话 最初的一步\n朋也由于之前父亲曾经酗酒后打伤了朋也，导致其右臂不能抬太高，由此失去了打篮球的资格的事和父亲之间的长达6年的互不理解让父子间的谈话变成了愤怒的争吵，朋也因此摔门而去，当他注意到的时候已经不知不觉来到了古河渚的住家。朋也呆呆地望着从渚的家里透出的光。他突然间听到了声音，回头时看见了带着平稳表情说着奇妙话语的渚的身影。\n\n第3话 流泪之后再来一次\n淋着雨等待朋也的渚病倒了。次日，朋也为了探望她而来到渚的家。虽然秋生和早苗说不必担心，但朋也仍然觉得是自己的责任。朋也的脑海中无法挥去抱着篮球淋雨的渚的模样。这时春原要求朋也去对付一个人，到了目的地后发现目标是一个女生，就是前几天单挑一群暴走族的那个暴力女，不知死活的春原被秒杀，这时朋也得知她叫坂上智代武帝，曾经一己之力打的自己原来上学的地方所有小混混不敢犯事，然后就转学来到了这里…中午藤林杏出现问朋也要不要吃便当，朋也同意了，去了发现椋也在那里，同时发现了那只害的春原被杏用字典击飞的野猪竟然是杏的宠物，名字叫牡丹。\n\n第4话 寻找伙伴吧\n和武帝单挑被再次击败后，朋也再次和藤林姐妹吃饭啧，姐妹通吃，结果贪吃的牡丹偷偷叼了一个鸡蛋卷，吃完后晕倒了，这才知道身为妹妹的藤林椋厨艺并不好…下午，渚和朋也展开了让演剧部复活的活动。但贴在校内的部员募集海报被撕了下来，渚也被学生会叫去了。朋也鼓励着被学生会禁止进行部员募集活动的渚。这时朋也在教室里发现了用美工刀雕刻星星的神秘少女……\n\n第5话 雕刻里的风景\n神秘少女名字叫做伊吹风子，而她的姐姐就是渚上一年级时的美术老师伊吹公子。但风子的话中总有些不可思议之处，而且也和渚所知道的老师妹妹的事情有些矛盾。怀着疑问的朋也和渚一起前去拜访风子的姐姐的家。\n\n第6话 姊妹的创立者祭\n风子化名矶贝风子神奇的矶贝家开始住在渚的家里，古河家又多了新的笑容。风子在学校仍然一如往常地给学生分发雕刻。看到风子努力的样子，渚为了让公子和风子见面，而邀请公子去参加创立者祭。\n\n第7话 星形的心情\n公子来到了创立者祭。渚由于风子和公子终于相见而感到高兴。但公子说出的令人意外的话语，让朋也和渚感到极度的不可思议与难过。面对这样的两人，公子询问他们是否知道风子在学校的传闻。\n\n第8话 消逝于黄昏中的风\n在幸村的安排下，在学校举办婚礼得到了许可，公子的婚礼日期也终于定下来了。风子继续为了准备婚礼而向学生们分发海星。朋也和渚注视着风子奔波的身影。但越来越多的学生不接受风子递出的海星，仿佛完全没有注意到风子的存在一样……\n\n第9话 直到梦的尽头\n朋也和渚带着风子潜入夜间的学校。3个人在演剧部的部室里，如同往常那样若无其事地互相开着玩笑。风子无意间提到朋也和渚应该互相称呼对方的名字，两人因此感到害羞。面对这样的两人，风子笑着说希望他们能像公子和芳野那样幸福。但随后风子消失并被朋也和渚遗忘。婚礼如期进行，朋也和渚也在幸村的提醒下再度想起风子，而收到风子的星星的人都来到学校为新人祝福……\n\n第10话 天才少女的挑战\n渚再次开始了恢复演剧部的部员募集。朋也看到毫无进展的状况，想起了可能成为潜在的新部员的某位少女。她就是一直不去上课、总在图书馆读书的一之濑琴美。\n\n第11话 放学后的狂想曲\n琴美从音乐室借来了小提琴。开心的琴美开始给大家演奏小提琴，音色让周围的人为之一震真•对军宝具。杏想要将琴美的注意力从小提琴上转移开，于是让她进行"搞笑"的练习，但为了满足琴美希望更多的人聆听小提琴演奏的愿望，小提琴的演奏会还是开始举办了收命的演奏会。\n\n第12话 被隐藏的世界\n小提琴演奏会结束，琴美归还了借来的小提琴。她和借给她小提琴的仁科成为了朋友，也开始去教室上课了。总是在图书馆独处的琴美周围也开始聚集起了人群成绩全校第一颜值出众家里有钱的美烧酒身边怎么会少人！。\n\n第13话 回忆中的庭园\n朋也前去琴美家探望时，觉得琴美的家和庭园有些似曾相识。偷偷进到家里看着琴美的朋也，发觉自己在小时候曾经遇到过琴美。幼时的记忆复苏了。面对开口发问的朋也，琴美开始讲述双亲的事……\n\n第14话 万有理论\n怀着想要为琴美做些什么的心情，渚和杏、椋开始为准备琴美的生日而行动，朋也也开始整理琴美家荒废的庭园。渚她们也抽出时间帮助朋也，并呼唤呆在家里的琴美，但没有得到回复，就在陷入僵局时，琴美父母所在研究所的同事带来了她双亲的遗物……\n\n第15话 困扰的问题\n为了使演剧部复活，杏、椋、琴美以部员的身份暂时挂名。如此一来活动所必需的人数就集齐了，接下来只要决定顾问老师，就能让演剧部复活了。渚她们想让幸村担任顾问，于是去拜托他，但……\n\n第16话 3 on 3\n春原的妹妹芽衣来找哥哥了。由于朋也的恶作剧，春原并不知道芽衣过来的事情。在春原不在时，芽衣熟练地整理了春原邋遢的房间，如此可靠的她简直让人不敢相信是那个春原的妹妹。朋也和渚看着芽衣产生了如此的感慨。之后，毫不知情的春原回来了……国欠妹！！！！\n\n第17话 不在的空间\n看了朋也他们与篮球部的比赛的仁科等人，向渚提出了顾问兼任的提案。如此一来演剧部就能够复活了，渚等人为此感到高兴。渚立刻前去学生会说明此事，但学生会不认同顾问的兼任。之后，在和学生会的谈话结束后，渚倒下了。\n\n第18话 逆转的秘技\n在与外校生的打架骚动中袒护了智代的朋也受到停学处分。智代为此感受到责任，并犹豫着该不该说出真相，朋也则对她说，希望她就算是为了演剧部也绝对要当上学生会长。另一方面，渚从倒下的那一天开始，就一直没有来学校。\n\n第19话 新的生活\n为了准备学园祭，演剧部终于开始活动。此时，朋也要接受有关将来出路的家庭访问。朋也不停地找理由逃避。但他在逃走时被渚发现，并被渚一直跟到家里。在那里，渚知道了朋也与父亲之间的关系。\n\n第20话 隐藏的过去\n渚为了准备学园祭开始练习。但她想要表演的演剧，是小时候听来的连名字都不知道的故事。那是个非常悲伤的冬日幻想物语，讲述了一个被遗留在世界上唯一的女孩的故事其实少女就是朋也和渚的孩子汐啦……\n\n第21话 面向学园祭\n学园祭越来越近了。除了要演出的渚以外，杏她们也决定了音响和照明的负责人，演剧部的活动逐渐走上正轨。早苗也来负责服装，秋生也为了帮助渚而借给她演剧的录像带。但看了录像的渚却接二连三地飞出爆炸性发言。\n\n第22话 形影成双\n学园祭当天。渚在最糟糕的时机得知了双亲的过去—原来父亲是之前的人气演员，母亲是中学教师，为了渚而放弃了未来那样的未来不是我想要的未来！。朋也则拼命鼓励着她。但渚认为自己不光给双亲，还给朋也他们添了麻烦，因而不断地自责。舞台的正式表演上，自责的渚不停的流泪，就在话剧部的大家都以为演出就这样完了的时候，古河秋生，之前的人气演员，渚的父亲，闯了进来……\n\n番外篇 暑假的故事\n学园祭结束了，朋也迎来了高中最后的夏天。即使到了暑假，3年级也要每天去补习，但朋也在渚的面前根本没法偷懒。这时，春原的妹妹芽衣又过来玩了。\n\n另一个世界 智代篇\n成绩优异作风优秀的坂上智代竟然也谈恋爱？！冰山美人的男朋友竟然是不良学生冈崎朋也？！两个人的故事究竟是怎样的？他们的未来又是什么样？！故事就此开始…',
    contributorIds: [1, 3],
    viewCount: 8888, sort: 20, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 12, title: '悠久之翼 ~前篇~', titleJp: 'ef - a tale of memories.', category: 'CLASSIC',
    cover: '/uploads/anime/ef-the-latter-tale.jpg', background: '/uploads/anime/ef_background.jpg', original: 'minori・鏡遊・御影', director: '大沼心',
    writer: '高山カツヒコ', episodes: 12, airDate: '2007年10月6日', airWeekday: '星期六', production: 'SHAFT',
    storyboard: '大沼心(1,7,12)、帆村壮二 [ 新房昭之 ](2)、上坪亮樹(3)、森義博(4)、草川啓造(5,8,12)、宮崎修治(6,11)、島津裕行(9)、石倉賢一(10)',
    performance: '大沼心(1,2,7,12)、松澤建一(3)、上坪亮樹(4)、飯村正之(5)、宮崎修治(6,11)、高島大輔(8)、森義博(9)、北川正人(10)、石倉賢一(10)',
    music: '天門・柳英一郎', charaOriginal: '七尾奈留、2C＝がろあ', charaDesign: '杉山延寛', seriesComposition: '高山カツヒコ',
    artDirector: '加藤恵', colorDesign: '日比野仁', chiefAnimationDirector: '杉山延寛',
    animationDirector: '杉山延寛、潮月一也、古川英樹、田畑昭、中山初絵、伊藤良明、田中穣、清水慶太、宮崎修治、潮月一也、玉木慎吾、沼津雅人、西田美弥子',
    photographyDirector: '内村祥平', planning: '川村明廣、酒井伸和、太布尚弘、及川武', alias: '悠久之翼1、ef1',
    quote: '忘れたくない思い、ありますか？你有不想忘却的回忆吗？',
    synopsis: '这是曾经因遭受地震和大火侵袭，而一度坍塌的街道——音羽。而这条街现在，犹如从欧洲的童话中飞出来一般，作为非常美丽的街景复苏了。就像是，将那不吉祥的灾难的记忆掩盖起来一般……在这片重生的土地上，一个童话般的恋爱物语开始了…',
    content: 'ef系列动画版分为2007年10月开始放送的 ef - a tale of memories. 与2008年10月放送的第二季 ef - a tale of melodies.。制作由大沼心监督, 新房昭之监修. \nef - a tale of memories.共12集, 于2007年10月6日开始放送，同年12月25日播放完结。着重讲述了新藤千寻和宫村宫子的主线故事。故事内容由the first tale的第一、二章及the latter tale的第三章改编而成然而the latter tale 08年5月才发售。动画中主要以英文作为标题与背景表达，可能是以千寻的英文小说为主，op背景就是以千寻小说为主的英文，而剧情中间插入的一些英文句子也出自千寻小说。\nef - a tale of melodies.共12集,于2008年10月6日开始播放。主要描写的是羽山瑞希和雨宫优子在两个不同时代的主线剧情，相互穿插演绎。故事内容由the latter tale的第四章、第五章以及终章改编而成。相比于第一季，其对原作游戏的修改幅度比较大，并且加入了一些原创的情节。其第10集有黑白/彩色两个版本。动画中主要以德文作为标题与背景表达，很可能是要暗示音羽仿自德国海德堡的关系，其中也可能与久濑修一留学德国有关，op背景的文字除了一些英文标题皆为德文。\nef系列虽然是作为新房昭之徒弟的大沼心的第一部监督作品，但是其风格展现的淋漓尽致比如玩op和改剧情, 天门的音乐也恰到好处。\n本部作品的op值得品味, 第一季最后一集少女们挣脱锁链的场景可谓和剧情同步, 第二季更是随着剧情发展每一集的op几乎都在变化(新房师徒玩OP的巅峰之一).',
    contributorIds: [3],
    viewCount: 4560, sort: 19, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 13, title: '新世纪福音战士 EVA', titleJp: '新世紀エヴァンゲリオン', category: 'CLASSIC',
    cover: '/uploads/anime/EVA.jpg', background: '/uploads/anime/EVA_b.jpeg', original: 'GAINAX → 庵野秀明', director: '庵野秀明',
    writer: '庵野秀明、榎戸洋司等', episodes: 26, airDate: '1995年10月4日', airWeekday: '星期三', production: 'タツノコプロ、GAINAX',
    storyboard: '庵野秀明(1-2,7,10,14,20,23-26)、摩砂雪(1-2,6,11-12,19,24)、鶴巻和哉(3,16,20,22-23,25-26)、石堂宏之(3)、甚目喜一 [ 佐藤順一 ] (4-5,15,21)、杉山慶一(7)、樋口真嗣(8-9)、加賀剛(10)、岡村天斎(13,18)、小黒晃(17)、ビデオフォーマット版：摩砂雪(23)',
    performance: '鶴巻和哉(1-2,8,16,25-26)、石堂宏之(3,6,10,12,21)、加賀剛(4,10)、杉山慶一(5,7)、水島精二(9)、渡邊哲哉(11)、岡村天斎(13,18)、大塚雅彦(14,20)、安藤健(14)、羽生尚靖(15)、大原実(17)、摩砂雪(19,24,26)、高村彰(22)、増尾昭一(23)、ビデオフォーマット版：大塚雅彦(21)、鈴木俊二(21)、鶴巻和哉(22)',
    music: '鷺巣詩郎', charaDesign: '貞本義行', artDirector: '加藤浩', colorDesign: '高星晴美',
    animationDirector: '鈴木俊二(1,5,7,15,23)、本田雄(2,8,19,25)、細井信宏(3,6)、重田智(4,10,12,21)、長谷川眞也(9,16)、河口俊夫(11)、黄瀬和哉(13,18)、花畑まう [ 湖川友謙 ] (17,22)、鶴巻和哉(20)、摩砂雪(24)、ビデオフォーマット版：鈴木俊二(21)、本田雄(22)、貞本義行(22)、鶴巻和哉(22)',
    photographyDirector: '黒田洋一', planning: 'GAINAX / Project Eva.', alias: 'EVA、Shin Seiki Evangerion',
    quote: '新世纪福音战士如此成功很奇怪——所有角色（人）都病得很厉害！',
    synopsis: '2000年，一个科学探险队在南极洲针对被称作"第一使徒"亚当的"光之巨人"进行探险。在对其进行接触实验时，"光之巨人"自毁，从而发生了"第二次冲击"，进而导致世界大战。最后，人类人口减半，地轴偏转、气候改变。根据对"第二次冲击"的调查，联合国在日本箱根成立人工进化研究所（即 GEHIRN）从事EVA（指机器人）的发展研究，后GEHIRN利用在人工进化研究所下方发现的巨大空洞建造了总部。\n另一方面，联合国下属秘密组织SEELE为了使人类进化，开始实行人类补完计划，就是将所有人的灵魂汇集在一起，通过中和每个人的AT力场，使每个人回归LCL之海。\n2004年，EVA初号机进行启动试验时发生事故，碇真嗣的母亲碇唯消失，碇源渡开始执行"碇源渡版本的人类补完计划"。2010年，GEHIRN被改建成NERV。\n2015年开始，根据SEELE人类补完计划剧本的安排，一种巨型人形生物"使徒"开始在日本登陆，并向NERV总部进攻，NERV组织EVA消灭使徒。在NERV与使徒作战的同时，碇源渡秘密地执行它自己的计划。随着时间推移，碇源渡的计划逐渐被SEELE发现，NERV与SEELE产生了矛盾并不断恶化。\nDirector\u2019s Cut版本的第21～24集由剧场版《Death》篇剪辑而成，收录在20周年纪念版的蓝光中。',
    content: '新世纪福音战士，是日本历史上少有的能带来广泛社会影响的现象级作品，被公认为日本最伟大的ACG圈钱作品之一。作品应用了当时颇具革命性的意识流手法，扑朔迷离、庞大复杂的故事情节，大量的神秘学、哲学、心理学概念，以及宗教符号的使用，都使得其在日本国内和国际上引起了巨大反响和争议，并成为日本动画史上的一座里程碑。但主要是因为后面没钱了真实原因是工期不够\n虽然是在作者十分困顿的情况下随着播放进度边写边放送所作，但整体作品仍然具有完善和有趣的剧情。作品初期注重描写人物对话及战斗，随后通过情节的推进开始着重于人物内心的叙述。在TV版的后期阶段，借助强烈象征意味的画面与配音的闪现，以此表现出的精神分析性的叙述可以说是本作的标志。在TV放送后，日本产生"社会现象"级别的巨大回响与冲击，《新世纪福音战士》也成为当时的热议话题。即便在新剧场版陆续放出的现在，仍然拥有广泛的衍生品商品和火爆的人气。\n新世纪福音战士作品分为两大部分，分别是被称作TV版的1995年和2003的电视放送版以及简称为新剧场版的新世纪福音战士新剧场版系列。两部作品虽然人物设定与事件大体相似，但是剧情毫无关联，登场人物的个性也不完全一样。\n与此同时，事件的因果，造成的影响，人物的心理状态等等可以说是《EVA》灵魂的东西也完全不一样，可以说是平行世界发生的故事，所以一般将它们看做两部作品。也有一种说法是TV版和新剧场版有时间先后顺序的关系，这种说法有一定证据但是并没有证明。\n死与新生包含死（DEATH）部分与（REBIRTH）新生部分，现在流传的是第一部分（DEATH）修正后的第二版本。"DEATH"部分实际上是第一话到第二十四话的总集篇，并没有新剧情加入，但是添加了新的镜头，包括"第二次冲击的始末，碇源渡手中的\u2019亚当\u2019，碇唯与冬月的对话，以及碇真嗣，绫波丽，明日香与渚熏的音乐演奏练习等部分"。',
    contributorIds: [3],
    viewCount: 9999, sort: 18, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 14, title: '命运石之门', titleJp: 'STEINS;GATE', category: 'CLASSIC',
    cover: '/uploads/anime/STEINS_GATE.jpg', background: '/uploads/anime/ST_b.jpg', original: '5pb./Nitroplus', director: '佐藤卓哉、浜崎博嗣',
    writer: '花田十輝', episodes: 25, airDate: '2011年4月6日', airWeekday: '星期三', production: 'WHITE FOX',
    storyboard: '浜崎博嗣(1,14,17,23β,24)、佐藤卓哉(2,12-13,17,23β,24)、サトウシンジ(3)、おざわかずひろ(4,10,17,23,23β)、池田重隆(5,11,17)、村川健一郎 [ 川村賢一 ](6,16,21)、加藤敏幸(7,18)、若林漢二(8,14)、小林智樹(9,15,25)、須間雅人 [ 小林常夫 ](19,22)',
    performance: 'おざわかずひろ(1,4,10,18,23)、池田重隆(2,5)、若林漢二(3,8,14,20-21,24,OP)、平向智子(6,16)、下田久人(7,13)、小林智樹(9,15,22)、小林公二(11,17,24)、立川譲(12)、土屋浩幸(19)、美甘義人(20)、佐藤卓哉(24)、浜崎博嗣(24)',
    music: '阿保剛、村上純', charaOriginal: 'huke', charaDesign: '坂井久太', seriesComposition: '花田十輝',
    artDirector: '衛藤功二', colorDesign: '佐藤美由紀', chiefAnimationDirector: '坂井久太',
    animationDirector: '坂井久太、中村和久、池上たろう、佐藤天昭、中田正彦、青野厚司、武本大介、稲吉智重、稲吉朝子、川田剛、いがりたかし、松原一之、吉井弘幸、新井伸浩、竹田逸子、山本善哉、泉保良輔',
    photographyDirector: '中村圭介', planning: '安田猛、及川武', alias: '石头门、斯坦因之门',
    quote: '跨越每一秒的世界线，只为了守护，你的笑容……',
    synopsis: '故事发生在「CHAOS;HEAD」的"涩谷崩坏"事件一年半之后的世界，而舞台则从涩谷转移到了秋叶原。主角冈部伦太郎是一位深度中二病的大学生，时常幻想自己身肩重任，并自称"狂气的疯狂科学家・凤凰院凶真"，不过说到底其作为不过就是在名为"未来道具研究所"中与两个伙伴开发着奇奇怪怪又不切实际的东西。然而，这样的他们却在偶然间发明出可以把电子讯息传送过去的时间机器。在他们对未来及过去知道得越多的同时，却不知道危难正渐渐临到自己身上……',
    content: '以秋叶原为据点的研究所（同好会）"未来道具研究所"的所长，自称"狂妄的疯狂科学家——凤凰院凶真"的，改不了中二病的，东京电机大学大一学生——冈部伦太郎和他的伙伴们终日重复着古怪发明的开发。2010年7月28日，冈部为了取得学分和同级的好友桥田至一同去了讲义会场，在那里，他们遇见了年仅18岁就成功在美国科学杂志刊登论文的天才少女牧濑红莉栖。然而古怪的是，冈部在几个小时前刚刚目睹了在RADIO会馆8楼倒在血泊中的牧濑。更让人费解的是，这一切在一周前发送给桥田的手机短信中都记载的清清楚楚。\n最后冈部查明，原来他们发明的其中一件发明装置偶然具有了向过去发送短信的功能，也就是具备了与时光机类似的机能。而这个时候的冈部连想都没想过这一个偶然的发明将会成为左右世界未来的钥匙。就这样，世纪大发明诞生了……',
    contributorIds: [3],
    viewCount: 7777, sort: 17, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 15, title: '白箱 SHIROBAKO', titleJp: 'SHIROBAKO', category: 'CLASSIC',
    cover: '/uploads/anime/SHIROBAKO.jpg', background: "/uploads/anime/shirobako_b'.jpg", original: '原创动画', director: '水岛努',
    writer: '横手美智子', episodes: 24, airDate: '2014年10月9日', airWeekday: '星期四', production: 'P.A.WORKS',
    storyboard: '水島努(1,24)、平井義通(2)、許琮(3,8,10,17,23-24)、岡村正弘(4)、湖山禎崇(5,9)、かおり(6)、菅沼芙実彦(7,13,15,23)、柿本広大(11)、倉川英揚(12,16,22)、駒井一也(14)、高村彰(18)、篠原俊哉(19)、成田歳法(20)、畑博之(21)',
    performance: '水島努(1)、菅沼芙実彦(2,7,14,22,24)、許琮(3,8,17,24)、かおり(4)、湖山禎崇(5,9)、守岡博(6)、今泉賢一(10)、高島大輔(11)、倉川英揚(12,23)、熨斗谷充孝(13,18)、横田一平(15)、太田知章(16,23)、平牧大輔(19)、菱川直樹(20)、畑博之(21)',
    music: '浜口史郎', charaOriginal: 'ぽんかん⑧', charaDesign: '関口可奈味', seriesComposition: '横手美智子',
    artDirector: '竹田悠介、垣堺司', colorDesign: '井上佳津枝', chiefAnimationDirector: '関口可奈味',
    animationDirector: '関口可奈味、秋山有希、大東百合恵、川面恒介、朱絃沰(주현우)、熊田明子、西畑あゆみ、森島範子、朴允玉(박윤옥)、神崎舞人、川口千里、鈴木理沙、川村夏生、竹田欣弘、齊藤佳子、しまだひであき、松坂定俊、野田康行、佐藤陽子、今泉賢一、酒井智史、宮川智恵子、渡辺佳奈子、佐野陽子、深澤謙二、武田牧子、容洪、徐正徳(서정덕)',
    photographyDirector: '梶原幸代', planning: '川村明廣、堀川憲司、臼井久人、武智恒雄、難波秀行、太布尚弘、安田猛、立本洋之',
    alias: 'Shirobako', quote: '动画的当下，就在这里。',
    synopsis: '本作的故事主要围绕追逐梦想的五名女生——宫森葵、安原绘麻、坂木静香、藤堂美沙、今井绿展开，是一部描写以白箱的完成为目标而奋斗的她们，每天遇到的麻烦以及策划工作时碰到的纠葛与挫折，还有制作组在制作作品时的团结和冲突的动画业界的日常的群像剧作品。',
    content: '"SHIROBAKO"是指映像业界所使用的装有录影带的白箱，也即是一部作品完成之时，制作者所得到的最初的成果物。与插画或摄影等制作华丽的贩售用包装相比，仅仅是装入白箱中的录影带或许显得很不起眼。但，在那里却满载着创作者们的心意。\n故事以5名追梦的女孩为中心，是一部描绘以完成白箱为目标而在动画业界奋斗的她们每天所经历的困境、在创作中发生的种种矛盾与挫折、以及在团体当中的合作与冲突这些动画业界日常的群像剧作品。以及，5人对达到共同向往的梦想的挑战。\n向着前方的希望延续梦想的成功物语。\n动画的当下，就在这里……。',
    contributorIds: [3],
    viewCount: 6540, sort: 16, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 16, title: '猫和老鼠', titleJp: 'Tom and Jerry', category: 'CLASSIC',
    cover: '/uploads/anime/Tom_and_Jerry.jpg', background: '/uploads/anime/taj.jpg', original: 'William Hanna, Joseph Barbera', director: 'William Hanna',
    writer: '', episodes: 161, airDate: '1940年2月10日', airWeekday: '', production: 'Warner Bros.',
    performance: '大沼心(1,2,7,12)、松澤建一(3)、上坪亮樹(4)、飯村正之(5)、宮崎修治(6,11)、高島大輔(8)、森義博(9)、北川正人(10)、石倉賢一(10)',
    music: 'Scott Bradley→Steven Konichek→Eugene Poddany / Dean Elliott / Carl Brandt→J. Eric Schmidt→Michael Giacchino',
    quote: 'Tom And Jerry、猫和老鼠',
    synopsis: 'Tom and Jerry is an American animated series of short films created in 1940 by William Hanna and Joseph Barbera. It centers on a rivalry between its two main characters, Tom Cat and Jerry Mouse, and many recurring characters, based around slapstick comedy.\nIn its original run, Hanna and Barbera produced 114 Tom and Jerry shorts for Metro-Goldwyn-Mayer from 1940 to 1957. During this time, they won seven Academy Awards for Animated Short Film, tying for first place with Walt Disney\u2019s Silly Symphonies with the most awards in the category. After the MGM cartoon studio closed in 1957, MGM revived the series with Gene Deitch directing an additional 13 Tom and Jerry shorts for Rembrandt Films from 1960 to 1962. Tom and Jerry then became the highest-grossing animated short film series of that time, overtaking Looney Tunes. Chuck Jones then produced another 34 shorts with Sib-Tower 12 Productions between 1963 and 1967. Two more shorts were produced, The Mansion Cat in 2001 and The Karate Guard in 2005, for a total of 163 shorts. Various shorts have been released for home media since the 1990s.',
    content: '《猫和老鼠》的故事情节是围绕着汤姆猫和杰瑞鼠展开的欢喜冤家故事。\n\n汤姆有一种强烈的欲望，总是不断努力的去捉同居一室的老鼠杰瑞，并不断地努力驱赶着这位讨厌的房客。尽管大多数情况下都以失败告终，但汤姆在追逐中得到的乐趣远远超过了捉住杰瑞。同时，汤姆在片中经常使用斧头、锤子、炸药、鞭炮等工具或陷阱来对付杰瑞。但杰瑞非常机灵，时而使汤姆的诡计适得其反。\n\n在这部动画中，没有动物世界中恃强凌弱的残酷，只有两个邻居之间的日常琐事和纷争。在一部分剧情中汤姆和杰瑞甚至是站在同一战线，对付自家的女主人或者某条大狗、再或者是某只黑猫。除此之外，《猫和老鼠》本身也是作品自己的作中作，在作品中关于看电影的演绎中，多次把往期影片直接当做片中放映的电影。\n\n《猫和老鼠》全系列几乎全部是哑剧模式，剧中通常只有很简单的情节、甚至没有一句对白，但是偶尔汤姆，杰瑞或者其他配角会说一两句话。这使得《猫和老鼠》系列成为极佳的二次创作素材，通常是配上搞怪台词用来演绎某些故事。经过《猫和老鼠》配成各种方言[3]，使得《猫和老鼠》在中国的家庭中人人知晓。',
    contributorIds: [3],
    viewCount: 12345, sort: 15, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 17, title: 'Fate/Stay Night', titleJp: 'フェイト/ステイナイト', category: 'CLASSIC',
    cover: '/uploads/anime/Fate_15th.webp', background: '/uploads/anime/Fate_15th.webp', original: '奈須蘑菇、TYPE-MOON', director: '山口祐司',
    writer: '', episodes: 24, airDate: '2006年1月6日', airWeekday: '星期五', production: 'Studio Deen',
    storyboard: '三浦貴博(0,3,10)、野中卓也(1,4,8)、笹嶋啓一(2)、白井俊行(5,12)、高橋タクロヲ(6,12)、栖原隆史(7,11)、宇田明彦(9)、高橋健二(11)、高橋健司',
    performance: '三浦貴博(0)、野中卓也(1,4,8)、栖原隆史(2,7,ED)、恒松圭(3)、白井俊行(5,12)、高橋健二(5)、高橋タクロヲ(6,12)、宇田明彦(9)、丸山裕介(10-11)',
    music: '深澤秀行', charaOriginal: '武内崇', charaDesign: '須藤友徳、田畑壽之、碇谷敦', seriesComposition: 'ufotable',
    artDirector: '衛藤功二', colorDesign: '千葉絵美、松岡美佳', chiefAnimationDirector: '須藤友徳、田畑壽之、碇谷敦',
    animationDirector: '須藤友徳、田畑壽之、佐藤哲人、茂木貴之、石塚みゆき、青木拓也、藤崎静香、碇谷敦、白井俊行、加藤やすひさ、菊池隼也、緒方美枝子、小船井充、清水慶太、辻雅俊',
    photographyDirector: '寺尾優一', alias: '命运之夜 无限剑制、Fate/stay night UBW', quote: '此为，贯彻信念的故事。',
    synopsis: '舞台は海と山に囲まれた都市・冬木市。\nそこで行われる、ある一つの儀式。\n手にした者の願いを叶えるという聖杯を実現させる為、聖杯に選ばれた七人の魔術師に、聖杯が選んだ七騎の使い魔を与える。\n騎士 "セイバー"\n槍兵 "ランサー"\n弓兵 "アーチャー"\n騎兵 "ライダー"\n魔術師 "キャスター"\n暗殺者 "アサシン"\n狂戦士 "バーサーカー"\nマスターは七つの役割を被った使い魔一人と契約し、七組は、聖杯を求め、最後の一組となるまで殺し合いを繰り広げる。\nその名は「聖杯戦争」。\n2014年 秋、開戦 ――。',
    content: '《Fate/stay night》（日语：フェイト/ステイナイト)，简称stay night，是由TYPE-MOON于2004年1月30日发售的PC平台文字冒险游戏/视觉小说，并于2005年10月28日发售FAN DISC《Fate/hollow ataraxia》。并有动画、漫画等衍生作品，并推出前传小说及动画《Fate/Zero》。\n由于广受欢迎，2005年10月28日发售Fan Disc「Fate/hollow ataraxia」。2006年1月播放Fate线电视动画，至同年6月全24集已经播放完毕。该游戏的PS2版本由角川书店发售。2010年1月23日剧场版Fate/stay night [Unlimited Blade Works]在日本本土上映[1]，BD-DVD于2010年9月30日正式发行。此电视动画版及剧场版均由STUDIO DEEN制作。\n2007年，本作在去除R18元素后被移植到PS2上，并新增语音、CG、演出效果、剧情及追加结局，此版本被称为「Réalta Nua」，其后此版本亦被重新移植到PC、PlayStation Vita及智能手机上。\n2013年7月，曾制作动画版《Fate/Zero》的ufotable宣布即将把本作重新动画化，于2014年1月30日公布为TV动画，7月27日的"Fate Project"最新情报发表会上宣布内容为「Unlimited Blade Works」路线，并将分割为2季播出，第1季于同年10月4日播出，第2季于2015年4月播出（参见：TV动画Fate/stay night [Unlimited Blade Works]）。同时宣布「Heaven\u2019s Feel」路线将制作成剧场版动画[2]，一共分为3章节。第1章《presage flower》于2017年10月14日上映[3]，第2章《lost butterfly》于2019年1月12日上映[4]，第3章《spring song》于2020年8月15日在日本、台湾地区同步上映。[5]。（参见：剧场版Fate/stay night[Heaven\u2019s Feel]）\n2024年6月28日，「Fate/Grand Order 迦勒底放送局 轻量版 FGO Fes. 2024＆游戏最新情报」宣布，将在2024年发售「Fate/stay night REMASTERED」，其基于2012年发售的PS VITA游戏《Fate/stay night [Réalta Nua]》制作，经过高清重置，并支持英语和简体中文，将于Steam®·Nintendo Switch™发售。',
    contributorIds: [1],
    viewCount: 5210, sort: 14, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 18, title: '花开伊吕波', titleJp: '花咲くいろは', category: 'CLASSIC',
    cover: '/uploads/anime/Hanasaku_Iroha.jpg', background: '/uploads/anime/YLB.webp', original: 'P.A.WORKS', director: '安藤真裕',
    writer: '', episodes: 26, airDate: '2011年4月3日', airWeekday: '星期日', production: 'P.A.WORKS',
    storyboard: '安藤真裕、安斎剛文、篠原俊哉、橋本昌和、橘正紀、許琮、岡村天斎、西村純二、入江泰浩',
    performance: 'かおり、倉川英揚、太田知章、安藤真裕、橋本昌和、篠原俊哉、西村純二、柿本広大、羽生尚靖、安斎剛文、守岡博、許琮',
    music: '浜口史郎', charaOriginal: '岸田メル', charaDesign: '関口可奈味', seriesComposition: '岡田麿里',
    artDirector: '東地和生；辅佐：平柳悟', colorDesign: '井上佳津枝', chiefAnimationDirector: '関口可奈味',
    animationDirector: '落合瞳、江森真理子、川面恒介、吉田優子、山内尚樹、鈴木美咲、伊藤依織子、三嶽理絵、小島明日香、福永純一、鍋田香代子、小川エリ、홍석표、丸山宏一、田中春香、肥塚正史、石井百合子、関口可奈味、大塚あきら、川口千里、天﨑まなむ、許宰銑、岡村正弘、大導寺美穂',
    photographyDirector: '並木智', planning: '角田博昭、沼生祐介、板橋秀徳、田口浩司、倉重宣之、木村康貴、米倉功人',
    alias: '花开物语、Hanasaku Iroha', quote: '花咲くいつか、某天会绽放的花朵',
    synopsis: '16岁少女松前緒花，平凡的日常生活在某天迎来了戏剧化的改变。\n生于东京的她离开熟悉的环境，来到祖母所经营的拥有大正浪漫氛围的山间温泉旅馆——喜翠庄打工，展开了崭新的生活。\n如同阳光下盛开的鲜花一般，相信梦想终有一天一定会开花……',
    content: '本作品是P.A.WORKS纪念10周年而特别制作。于2010年12月5日在石川县金泽市的金泽21世纪在美术馆宣布制作发布。动画监督则由曾执导《CANAAN》的安藤真裕担任。于2011年4月3日首播、9月25日播放完毕，全26话。是P.A.WORKS青春三部曲的第二部，其余两部为《真实之泪》《TARI TARI》；同时也是「工作中的女孩子」系列动画的第一部。\n\n突然的夜逃、突然的告白、以及突然的离别——。\n想要变得与至今为止的自己有所不同的这一梦想，突然间化为现实。\n我，松前绪花平凡的日常，在一天之内迎来了戏剧性的展开。\n离开了虽已习惯却不甚留恋的城镇，\n来到了素未谋面的外婆之处生活。\n洋溢着大正浪漫的温泉旅馆·喜翠庄。\n在那里遇到的人们。\n如同花的萌芽破土而出并知晓新的世界那般，\n我也开始了与至今为止完全不同的新生活。\n这或许会很辛苦吧。\n但，即使气馁、即使受挫、即使抽泣，明日也依然会到来。\n正因如此，我想要去努力，并变得闪耀起来。\n如同被太阳引导着绽放的花朵一般，\n希望总有一天，让大朵的花朵盛开……。',
    contributorIds: [3],
    viewCount: 3390, sort: 13, createTime: '2026-01-01 00:00:00',
  },
  {
    id: 19, title: '魔法少女小圆', titleJp: '魔法少女まどか☆マギカ', category: 'CLASSIC',
    cover: '/uploads/anime/R-C.jpg', background: '/uploads/anime/R-C.jpg', original: 'Magica Quartet', director: '新房昭之',
    writer: '虚渊玄', episodes: 12, airDate: '2011年1月7日', airWeekday: '星期五', production: 'SHAFT',
    storyboard: '新房昭之(NC)、芦野芳晴(1,2,3)、笹木信作(4,6,10,12)、小俣真一(5,8)、西田正義(7)、七嶋典子(9)、伊藤智彦(11)',
    performance: '宮本幸裕(1,12)、向井雅浩(2,9)、八瀬祐樹(3,10)、小俣真一(4)、間島崇寛(5)、浅利藤彰(6)、城所聖明(7)、川畑喬(8)、渡邉こと乃(11)',
    music: '梶浦由記', charaOriginal: '蒼樹うめ', charaDesign: '岸田隆宏', seriesComposition: '虚淵玄(Nitro+)',
    artDirector: '稲葉邦彦・金子雄司(1-6)、内藤健(7-12)', colorDesign: '日比野仁・滝沢いづみ', chiefAnimationDirector: '谷口淳一郎、高橋美香',
    animationDirector: '江畑諒真、高橋美香、実原登、鳥山冬美、潮月一也、神谷智大、小関雅、本多美乃、松本麻友子、小菅和久、宮嶋仁志、福永純一、小林亮、門智昭、近藤優次、松本朋之、片山みゆき、牧孝雄、松本元気、岩崎安利、伊藤良明、半澤淳、関口雅浩、宮前真一、藤澤俊幸、谷口淳一郎',
    photographyDirector: '江藤慎一郎', planning: '夏目公一朗、孝壽尚志、峯岸卓生、小坂崇氣、太布尚弘、久保田光俊、竹田靑滋',
    alias: 'Mahou Shoujo Madoka Magica、魔法少女小圆☆魔力', quote: '和我签订契约，成为魔法少女吧！',
    synopsis: '大好きな家族がいて、親友がいて、時には笑い、時には泣く、そんなどこにでもある日常。\n見滝原中学校に通う、普通の中学二年生・鹿目まどかも、そんな日常の中で暮らす一人。\nある日、彼女に不思議な出会いが訪れる。\nこの出会いは偶然なのか、必然なのか、彼女はまだ知らない。\nそれは、彼女の運命を変えてしまうような出会い――\nそれは、新たなる魔法少女物語の始まり――\n\n\n出生良好家庭，亲朋好友团聚，时哭时笑，这是谁都拥有的日常生活。\n市立见泷原中学的平凡初二女生鹿目圆，就是其中一位。\n一天，一个不可思议的人出现在她眼前。\n不知是偶然抑或注定，可以肯定的是，这相遇改变了她的命运，一个新的魔法少女故事亦随即开始。',
    content: '故事围绕着5位魔法少女依次登场而展开。出生于良好家庭，亲朋好友团聚，有哭有笑，是谁都拥有的日常生活。就读市立见泷原中学二年级的少女鹿目圆就是如此平凡地过着幸福的每一天。而一名神秘转学生晓美焰的出现，让圆的命运开始改变。\n\n鹿目圆和朋友在回家的途中发现焰竟然手持武器，猎杀一只伤痕累累，名为丘比的神秘生物。风波过后，丘比希望少女们和自己签订契约，成为"魔法少女"来保护人类。随着憧憬破灭于残酷的现实，鹿目圆的想法也受到魔法少女黑暗真相的无情冲击……',
    contributorIds: [3],
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
  { id: 1, postId: 1, userId: 2, username: 'test', content: '冰菓真的值得多刷，每次看都有新感受！', likeCount: 3, likedUsers: [1, 3, 4], createTime: '2026-01-06 08:00:00' },
  { id: 2, postId: 1, userId: 3, username: 'tachibana', content: '京阿尼的演出和米泽的推理确实是绝配。', likeCount: 5, likedUsers: [1, 2, 4, 5, 6], createTime: '2026-01-06 10:20:00' },
  { id: 3, postId: 1, userId: 4, username: 'user01', content: '补番清单+1，今晚就去看。', likeCount: 1, likedUsers: [1], createTime: '2026-01-07 21:40:00' },
  { id: 4, postId: 2, userId: 2, username: 'test', content: '有道理，但我觉得小火的作品质量也很高，不能一概而论。', likeCount: 2, likedUsers: [1, 5], createTime: '2026-01-11 09:00:00' },
  { id: 5, postId: 2, userId: 1, username: 'admin', content: '讨论度也是作品生命力的一部分嘛。', likeCount: 4, likedUsers: [2, 3, 4, 6], createTime: '2026-01-11 13:30:00' },
  { id: 6, postId: 3, userId: 1, username: 'admin', content: '这季度确实经费拉满，制作没得挑。', likeCount: 0, likedUsers: [], createTime: '2026-02-02 11:00:00' },
]

export const animeComments = [
  { id: 1, animeId: 11, userId: 2, username: 'test', content: 'CLANNAD 的渚线看得我眼泪止不住，京阿尼永远的神。', likeCount: 12, likedUsers: [1, 3, 4, 5, 6], createTime: '2026-02-01 20:00:00' },
  { id: 2, animeId: 11, userId: 4, username: 'user01', content: '团子大家族一响就绷不住了。', likeCount: 8, likedUsers: [1, 3, 5], createTime: '2026-02-02 21:30:00' },
  { id: 3, animeId: 13, userId: 3, username: 'tachibana', content: 'EVA 的意识和宗教符号，N刷都看不腻。', likeCount: 6, likedUsers: [1, 4], createTime: '2026-02-03 10:15:00' },
  { id: 4, animeId: 14, userId: 5, username: 'user02', content: '石头门前期慢热，后半直接封神。', likeCount: 9, likedUsers: [1, 2, 3], createTime: '2026-02-04 12:40:00' },
  { id: 5, animeId: 19, userId: 2, username: 'test', content: '和我签订契约，成为魔法少女吧！第三话直接起飞。', likeCount: 15, likedUsers: [1, 4, 5], createTime: '2026-02-05 18:20:00' },
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
