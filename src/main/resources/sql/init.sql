-- =============================================================
-- ProjectSekai-05 初始化脚本（MySQL 8.0，库: animeairi）
-- 用法: 在任意客户端直接执行即可（脚本自动创建并切换到数据库）
-- 说明: DROP + CREATE + INSERT 保证可重复执行，运行结果一致
-- =============================================================

CREATE DATABASE IF NOT EXISTS animeairi DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE animeairi;

-- =============================================================
-- 1. 系统用户表 sys_user
--    角色: USER 普通用户 / ADMIN 管理员 / SUPER_ADMIN 超级管理员
-- =============================================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(128) NOT NULL COMMENT '密码(SHA-256(salt+password))',
    nickname    VARCHAR(50)           DEFAULT NULL COMMENT '昵称',
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色: USER/ADMIN/SUPER_ADMIN',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 1正常 0禁用',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0未删 1已删',
    create_time DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户表';

-- 演示账号: 密码均为 123456 (SHA-256(salt+password))
INSERT INTO sys_user (id, username, password, nickname, role, status, create_time)
VALUES (1, 'admin', 'd4c4d76050f7aa79b0cae44a8cb961cb781ea6e271e83cd70e0f76f5fea0b340', '管理员', 'SUPER_ADMIN', 1, '2026-01-01 10:00:00'),
       (2, 'test', 'd4c4d76050f7aa79b0cae44a8cb961cb781ea6e271e83cd70e0f76f5fea0b340', '测试用户', 'USER', 1, '2026-01-02 11:00:00'),
       (3, 'tachibana', 'd4c4d76050f7aa79b0cae44a8cb961cb781ea6e271e83cd70e0f76f5fea0b340', 'Tachibana站长', 'ADMIN', 1, '2026-01-03 09:30:00'),
       (4, 'user01', 'd4c4d76050f7aa79b0cae44a8cb961cb781ea6e271e83cd70e0f76f5fea0b340', '追番萌新', 'USER', 1, '2026-02-10 20:15:00'),
       (5, 'user02', 'd4c4d76050f7aa79b0cae44a8cb961cb781ea6e271e83cd70e0f76f5fea0b340', '老婆党', 'USER', 1, '2026-03-05 14:40:00'),
       (6, 'user03', 'd4c4d76050f7aa79b0cae44a8cb961cb781ea6e271e83cd70e0f76f5fea0b340', '禁止摆烂', 'USER', 0, '2026-04-18 08:22:00');

-- =============================================================
-- 2. 动漫表 anime
--    分类 category: NEW 一月新番 / CLASSIC 经典动画
-- =============================================================
DROP TABLE IF EXISTS anime;
CREATE TABLE anime
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    title       VARCHAR(100) NOT NULL COMMENT '标题',
    title_jp    VARCHAR(100)          DEFAULT NULL COMMENT '日文名',
    category    VARCHAR(20)  NOT NULL DEFAULT 'NEW' COMMENT '分类: NEW/CLASSIC',
    cover       VARCHAR(255)          DEFAULT NULL COMMENT '封面URL',
    background  VARCHAR(255)          DEFAULT NULL COMMENT '网页背图URL(详情页大图背景)',
    original    VARCHAR(100)          DEFAULT NULL COMMENT '原作',
    director    VARCHAR(100)          DEFAULT NULL COMMENT '导演',
    writer      VARCHAR(100)          DEFAULT NULL COMMENT '脚本',
    episodes    INT                   DEFAULT NULL COMMENT '话数',
    air_date    VARCHAR(50)           DEFAULT NULL COMMENT '放送开始',
    air_weekday VARCHAR(20)           DEFAULT NULL COMMENT '放送星期',
    production  VARCHAR(100)          DEFAULT NULL COMMENT '制作公司',
    synopsis    TEXT COMMENT '简介',
    content     TEXT COMMENT '内容/分集介绍(支持换行)',
    storyboard  VARCHAR(500)          DEFAULT NULL COMMENT '分镜',
    performance VARCHAR(500)          DEFAULT NULL COMMENT '演出',
    music       VARCHAR(255)          DEFAULT NULL COMMENT '音乐',
    chara_original VARCHAR(255)       DEFAULT NULL COMMENT '人物原案',
    chara_design   VARCHAR(255)       DEFAULT NULL COMMENT '人物设定',
    series_composition VARCHAR(255)   DEFAULT NULL COMMENT '系列构成',
    art_director     VARCHAR(255)     DEFAULT NULL COMMENT '美术监督',
    color_design     VARCHAR(255)     DEFAULT NULL COMMENT '色彩设计',
    chief_animation_director VARCHAR(255) DEFAULT NULL COMMENT '总作画监督',
    animation_director VARCHAR(500)   DEFAULT NULL COMMENT '作画监督',
    photography_director VARCHAR(255) DEFAULT NULL COMMENT '摄影监督',
    planning          VARCHAR(255)    DEFAULT NULL COMMENT '企画',
    alias             VARCHAR(255)    DEFAULT NULL COMMENT '别名',
    quote             VARCHAR(500)    DEFAULT NULL COMMENT '语录',
    view_count  BIGINT       NOT NULL DEFAULT 0 COMMENT '浏览量',
    sort        INT          NOT NULL DEFAULT 0 COMMENT '列表排序(小在前)',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0未删 1已删',
    create_time DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_category (category),
    KEY idx_sort (sort),
    KEY idx_title (title)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='动漫表';

-- 种子数据（与前端 Mock db.js 一致，ID 对齐）
INSERT INTO anime (id, title, title_jp, category, cover, background, original, director, writer, episodes, air_date, air_weekday, production, synopsis, content, view_count, sort, create_time)
VALUES (1, '葬送的芙莉莲Ⅱ', '葬送のフリーレン 第2期', 'NEW', '/uploads/anime/Frieren2nd.jpg', NULL, '山田鐘人・アベツカサ（小学館）', '北川朋哉', '鈴木智尋', 12, '2026年1月16日', '星期五', 'MADHOUSE',
        '打倒了魔王的勇者一行人的后日谈——在"那之后"的故事。身为魔法使的芙莉莲是一位精灵，她和另外三人有着不一样的地方。对于生活在"之后"的世界、感受到的事情有着不一样的看法……残存世间的人们所编织的，葬送与祈祷相伴的故事—— 从"冒险的结束"开始了。',
        '精灵魔法使芙莉莲与勇者辛美尔一行，在打倒魔王之后各自踏上旅途。
身为长寿种族的芙莉莲，以人类数十年的寿命为参照，重新审视那些一度被忽略的日常与羁绊。
第二季继续讲述芙莉莲在旅途中的邂逅与告别，以及那跨越时间的情感与魔法。',
        2333, 10, '2026-01-01 00:00:00'),
       (2, '中国奇谭 第二季', '中国奇谭 第二季', 'NEW', '/uploads/anime/chinese_ancient.jpg', NULL, '上海美术电影制片厂', '陈莲华、胡睿等12位导演', '', 9, '2026年1月1日', '星期四', '上海美术电影制片厂、上影元、bilibili',
        '动画短片集《中国奇谭2》由9部风格迥异的短片组成：《耳中人》《小雪》《拜山》《如何成为三条龙》《大鸟》《三郎》《今日动物园》《刑天》《大贵人》。12位导演在延续中式想象力的基础上，探索多元题材与风格，挖掘中式内核表达，同时增强了现实关照和寓言属性，在奇幻语境中探寻现实命题。',
        '2023年9月26日，哔哩哔哩宣布启动《中国奇谭》第二季，由上海美术电影制片厂、上影元、bilibili、陈廖宇工作室出品。
作品汇集12位导演，创作9部风格迥异的短片。第二部的故事将走入烟火人间，在延续"用中国方式讲中国故事"的基础上，更侧重对人的自我身份、家庭关系、社会关系的思考和探讨。',
        1888, 9, '2026-01-01 00:00:00'),
       (3, '炎炎消防队Ⅲ', '炎炎ノ消防隊 参ノ章', 'NEW', '/uploads/anime/Enen_no_Shouboutai3rd.jpg', '/uploads/anime/ench.webp', '大久保篤', '南川達馬', '', 12, '2026年1月9日', '星期五', 'david production',
        '世界各地的"人體自燃現象"使城市化为火海，特殊消防队肩负起消灭"焰人"、揭露真相的使命。第三季中，森罗日下部等人将逐步逼近传教的背后隐藏的真相。',
        '特殊消防队第八大队的少年队员们，在不断与焰人战斗的同时，也逐渐触及到这个世界火灾现象背后的巨大阴谋。',
        1450, 8, '2026-01-01 00:00:00'),
       (4, 'Fate/strangeFake', 'フェイト／ストレンジフェイク', 'NEW', '/uploads/anime/Fate_strange_Fake.jpg', '/uploads/anime/FSF.webp', '奈須蘑菇（原作：成田良悟）', '榎戸骏', '', 12, '2026年1月3日', '星期六', 'A-1 Pictures',
        '伪圣杯战争在美利坚合众国西部城市史诺菲尔德展开。本不该存在的"假货"圣杯战争，将真正的英灵与形形色色的魔术师卷入一场混乱而危险的厮杀之中。',
        '成田良悟原作的群像剧，以夸张的展开与角色魅力著称，本作将正典圣杯战争的设定进一步放大，节奏激烈。',
        2021, 7, '2026-01-01 00:00:00'),
       (5, '咒术回战Ⅲ', '呪術廻戦 第3期', 'NEW', '/uploads/anime/Jujutsu_Kaisen3rd.jpg', '/uploads/anime/juju.jpeg', '芥見下々', '朴性厚', '', 12, '2026年1月14日', '星期三', 'MAPPA',
        '以诅咒为食的咒术师与咒灵之间的战斗持续升级。虎杖悠仁与伙伴们在涩谷事变之后，迎来了更加残酷的展开。',
        '咒术师与咒灵之间暗流涌动，虎杖、伏黑、钉崎等人的命运在巨大的恶意面前不断被推向极限。',
        3120, 6, '2026-01-01 00:00:00'),
       (6, '少女与战车 lovelove大作战 第2幕', 'ガールズ&パンツァー もっとらぶらぶ作戦です! 第2幕', 'NEW', '/uploads/anime/lovelove.jpg', '/uploads/anime/GupKV.webp', '「ガールズ＆パンツァー」シリーズ', '下田正美', '木村暢', 1, '2026年1月30日', '星期五', 'P.A.WORKS×アクタス',
        '因为讨厌战车道，才转学到没有战车道的大洗女子学园的西住美穗，没想到才刚刚转学过来就被学生会长叫了过去，会长要求美穗选择战车道的修行并且还要在全国大会中出场。队伍中聚集的成员全部都是超个性派——花道本家的大小姐五十铃华、热衷于恋爱的武部沙织、对战车无比狂热的秋山优花里、早上起不来的优等生冷泉麻子。',
        '希望与朋友们一起快乐的度过高中生活的美穗，到底能否如愿~',
        980, 5, '2026-01-01 00:00:00'),
       (7, '机动战士高达 剧场版', '機動戦士ガンダム 閃光のハサウェイ', 'NEW', '/uploads/anime/MOBILE_SUIT_GUNDAM_HATHAWAY_The_Sorcery_of_Nymph_Circe.jpg', '/uploads/anime/700.jpg', '富野由悠季', '村濑修功', '', 1, '2026年1月30日', '星期五', 'サンライズ',
        '剧场版动画，讲述哈萨维·诺亚继承父亲意志，以"马夫蒂"之名向地球联邦发起革命的战斗物语。',
        '宇宙世纪的反抗之焰在星光中燃烧，哈萨维的决断将改写世界的未来。',
        1277, 4, '2026-01-01 00:00:00'),
       (8, '我推的孩子Ⅲ', '【推しの子】第3期', 'NEW', '/uploads/anime/oshi_no_ko3rd.jpg', NULL, '赤坂アカ、横枪メンゴ', '平牧大輔', '', 12, '2026年1月14日', '星期三', '動画工房',
        '演艺圈与偶像的光与影。星野爱之子的复仇与成长物语，第三期将走向更加炽热的舞台。',
        '阿奎亚与露比各自追逐着目标，在闪耀的聚光灯下，隐藏的真相逐渐浮出水面。',
        2680, 3, '2026-01-01 00:00:00'),
       (11, 'CLANNAD', 'CLANNAD -クラナド-', 'CLASSIC', '/uploads/anime/CLANNAD.jpg', '/uploads/anime/CLANNAD_background.png', 'Key/VISUAL ARTS', '石原立也', '志茂文彦', 23, '2007年10月4日', '星期四', '京都アニメーション',
        '故事发生在一个小镇上，冈崎朋也是光坂高中在校生，因为家庭原因他一直过着浑浑噩噩的生活。某天的上学途中，在樱花飞舞的坡道上，他邂逅了一名少女——古河渚，从此他的生活发生改变。渚因病休学一年，重返校园的她对周围环境感到相当不适应。她想加入戏剧部，然而戏剧部早已休部。朋也决定帮助她一起开展戏剧部的活动，二人关系变得越发亲密。',
        '第1话 樱花飞舞的坡道上
在升学学校就读的高中3年生冈崎朋也，过着浑浑噩噩的每一天。直到那一天，在通往学校的坡道上，朋也遇到了一位少女……

第2话 最初的一步
朋也由于之前父亲曾经酗酒后打伤了朋也，导致其右臂不能抬太高，由此失去了打篮球的资格的事和父亲之间的长达6年的互不理解让父子间的谈话变成了愤怒的争吵……

第3话 流泪之后再来一次
淋着雨等待朋也的渚病倒了。次日，朋也为了探望她而来到渚的家……
更多精彩内容请前往动画主页查看。',
        8888, 20, '2026-01-01 00:00:00'),
       (12, '悠久之翼 ~前篇~', 'ef - a tale of memories.', 'CLASSIC', '/uploads/anime/ef-the-latter-tale.jpg', '/uploads/anime/ef_background.jpg', 'minori・鏡遊・御影', '大沼心', '高山カツヒコ', 12, '2007年10月6日', '星期六', 'SHAFT',
        '这是曾经因遭受地震和大火侵袭，而一度坍塌的街道——音羽。而这条街现在，犹如从欧洲的童话中飞出来一般，作为非常美丽的街景复苏了。就像是，将那不吉祥的灾难的记忆掩盖起来一般……在这片重生的土地上，一个童话般的恋爱物语开始了…',
        'ef系列动画版分为2007年10月开始放送的 ef - a tale of memories. 与2008年10月放送的第二季 ef - a tale of melodies.。制作由大沼心监督, 新房昭之监修。着重讲述了新藤千寻和宫村宫子的主线故事。',
        4560, 19, '2026-01-01 00:00:00'),
       (13, '新世纪福音战士 EVA', '新世紀エヴァンゲリオン', 'CLASSIC', '/uploads/anime/EVA.jpg', '/uploads/anime/EVA_b.jpeg', 'GAINAX → 庵野秀明', '庵野秀明', '庵野秀明、榎戸洋司等', 26, '1995年10月4日', '星期三', 'タツノコプロ、GAINAX',
        '2000年，一个科学探险队在南极洲针对被称作"第一使徒"亚当的"光之巨人"进行探险。2015年开始，一种巨型人形生物"使徒"开始在日本登陆，并向NERV总部进攻，NERV组织EVA消灭使徒。作品应用了当时颇具革命性的意识流手法，扑朔迷离、庞大复杂的故事情节，大量的神秘学、哲学、心理学概念，成为日本动画史上的一座里程碑。',
        '新世纪福音战士，是日本历史上少有的能带来广泛社会影响的现象级作品，被公认为日本最伟大的ACG圈钱作品之一。作品初期注重描写人物对话及战斗，随后通过情节的推进开始着重于人物内心的叙述。在TV版的后期阶段，借助强烈象征意味的画面与配音的闪现，以此表现出的精神分析性的叙述可以说是本作的标志。',
        9999, 18, '2026-01-01 00:00:00'),
       (14, '命运石之门', 'STEINS;GATE', 'CLASSIC', '/uploads/anime/STEINS_GATE.jpg', '/uploads/anime/ST_b.jpg', '5pb./Nitroplus', '佐藤卓哉、浜崎博嗣', '花田十輝', 25, '2011年4月6日', '星期三', 'WHITE FOX',
        '秋叶原的"未来道具研究所"，冈部伦太郎偶然间发明了可以向过去发送邮件的"时间机器"。然而，改变过去的选择，将引向无法挽回的命运的分歧。',
        '游戏改编动画。时间旅行的经典之作，从第12话开始的展开令人窒息，被称为"慢热神作"。',
        7777, 17, '2026-01-01 00:00:00'),
       (15, '白箱 SHIROBAKO', 'SHIROBAKO', 'CLASSIC', '/uploads/anime/SHIROBAKO.jpg', '/uploads/anime/shirobako_b''.jpg', '原创动画', '水岛努', '横手美智子', 24, '2014年10月9日', '星期四', 'P.A.WORKS',
        '以动画制作现场为舞台，讲述5位少女追逐动画梦想的故事。真实而热血地描绘了制作现场的欢笑与泪水，是献给所有动画从业者与爱好者的情书。',
        '立志进入动画行业的宫森葵与同伴们，在制作现场经历各种突发状况，一步步成长。业界科普与青春物语的完美结合。',
        6540, 16, '2026-01-01 00:00:00'),
       (16, '猫和老鼠', 'Tom and Jerry', 'CLASSIC', '/uploads/anime/Tom_and_Jerry.jpg', '/uploads/anime/taj.jpg', 'William Hanna, Joseph Barbera', 'William Hanna', '', 161, '1940年2月10日', '', 'Warner Bros.',
        '汤姆与杰瑞这对欢喜冤家的无休止追逐，跨越了半个多世纪的经典喜剧动画，陪伴了无数人的童年。',
        '1940年首播，多次获得奥斯卡最佳动画短片奖，以夸张的肢体喜剧与精妙的配乐闻名于世。',
        12345, 15, '2026-01-01 00:00:00'),
       (17, 'Fate/Stay Night', 'フェイト/ステイナイト', 'CLASSIC', '/uploads/anime/Fate_15th.webp', '/uploads/anime/Fate_15th.webp', '奈須蘑菇、TYPE-MOON', '山口祐司', '', 24, '2006年1月6日', '星期五', 'Studio Deen',
        '圣杯战争——七位御主与七位从者围绕可实现愿望的圣杯展开的厮杀。卫宫士郎在意外中召唤出剑士英灵Saber，被卷入这场残酷的战争。',
        'TYPE-MOON同名游戏改编。王与少年，理想与现实碰撞的壮阔物语就此展开。',
        5210, 14, '2026-01-01 00:00:00'),
       (18, '花开伊吕波', '花咲くいろは', 'CLASSIC', '/uploads/anime/Hanasaku_Iroha.jpg', '/uploads/anime/YLB.webp', 'P.A.WORKS', '安藤真裕', '', 26, '2011年4月3日', '星期日', 'P.A.WORKS',
        '因为母亲的突然出走，少女松前绪花来到祖母经营的温泉旅馆"喜翠庄"开始打工生活。在旅馆的忙碌日常中，绪花努力绽放属于自己的花朵。',
        'P.A.WORKS的青春三部曲之一，细腻描绘了温泉旅馆中的成长与羁绊。',
        3390, 13, '2026-01-01 00:00:00'),
       (19, '魔法少女小圆', '魔法少女まどか☆マギカ', 'CLASSIC', '/uploads/anime/R-C.jpg', '/uploads/anime/R-C.jpg', 'Magica Quartet', '新房昭之', '虚渊玄', 12, '2011年1月7日', '星期五', 'SHAFT',
        '温柔少女鹿目圆在神秘的转学生晓美焰与可爱生物丘比的影响下，即将成为魔法少女。然而，成为魔法少女的代价，远比想象中残酷。',
        '被誉为"魔法少女的成人礼"的颠覆性名作，虚渊玄剧本 × 新房昭之演出 × 梶浦由记音乐，第3话开始剧情急转直下。',
        8900, 12, '2026-01-01 00:00:00');

-- CLANNAD 补充制作阵容与语录（源自素材页 CLANNAD.html）
UPDATE anime
SET storyboard    = '石原立也(1,4,8)、石立太一(2,8,14)、坂本一也(3,15,17,20)、荒谷朋恵(5,11)、高雄統子(6,12,18)、北之原孝將(7,13,19)、三好一郎 [ 木上益治 ](9)、武本康弘(10,16,22)、米田光良(21)、山田尚子(23)',
    performance   = '石原立也(1,4)、北之原孝將(2,7,13,19)、三好一郎 [ 木上益治 ](3,9)、荒谷朋恵(5,11)、高雄統子(6,12,18)、石立太一(8,14,20)、武本康弘(10,16,22)、米田光良(15,21)、山田尚子(17,23)',
    music         = '折戸伸治、戸越まごめ、麻枝准',
    chara_original = '樋上いたる',
    chara_design  = '池田和美',
    series_composition = '志茂文彦',
    art_director  = '篠原睦雄',
    color_design  = '竹田明代',
    chief_animation_director = '池田和美',
    animation_director = '池田和美、高橋博行、植野千世子、西屋太志、高橋真梨子、池田晶子、堀口悠紀子、秋竹斉一、坂本一也',
    photography_director = '中上竜太',
    planning      = '馬場隆博、中嶋嘉美',
    alias         = 'クラナド、团子大家族',
    quote         = '如果您愿意的话，让我带您去吧，这座小镇，愿望实现的地方……'
WHERE id = 11;

-- =============================================================
-- 3. 论坛帖子表 forum_post
--    状态 status: 0待审核 / 1已发布 / 2已驳回
--    置顶 top: 0否 / 1是
-- =============================================================
DROP TABLE IF EXISTS forum_post;
CREATE TABLE forum_post
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    user_id       BIGINT       NOT NULL COMMENT '作者ID',
    title         VARCHAR(60)  NOT NULL COMMENT '标题',
    content       TEXT         NOT NULL COMMENT '正文(多段,空行分段)',
    source_url    VARCHAR(500) NOT NULL DEFAULT '' COMMENT '来源链接',
    status        TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0待审核 1已发布 2已驳回',
    reject_reason VARCHAR(255) NOT NULL DEFAULT '' COMMENT '驳回原因',
    top           TINYINT      NOT NULL DEFAULT 0 COMMENT '是否置顶: 0否 1是',
    view_count    BIGINT       NOT NULL DEFAULT 0 COMMENT '浏览量',
    reply_count   INT          NOT NULL DEFAULT 0 COMMENT '回复数(冗余计数)',
    deleted       TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0未删 1已删',
    create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_top_time (top, create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='论坛帖子表';

-- 种子数据（与前端 Mock db.js 一致，ID 对齐）
INSERT INTO forum_post (id, user_id, title, content, source_url, status, reject_reason, top, view_count, reply_count, create_time)
VALUES (1, 1, '看完冰菓的感受',
        '冰果艺术品般的演出作画音乐，米泽老师的推理剧情我不必多说，这里说一下我个人的感受。

我是考完中考，出分前每天深夜看几集慢慢看完的，特别还是在我妈同事的家里面(因为我不是走读的)。在万籁俱寂的夜晚，躺在陌生的床上，听着里面的古典音乐，欣赏京阿尼大师级别的演出，我也渐渐的进到了那个叫做神山的小镇里，沉浸在连续不断的学校日常，体会着四人的喜怒哀乐。

当第22集最后几个小提琴音收尾，蔷薇色的晚霞占满屏幕，君にまつわるミステリー的前奏响起……',
        'https://bgm.tv/blog/358002', 1, '', 1, 1520, 3, '2026-01-05 20:30:00'),
       (2, 1, '火，是检验作品质量最重要标准',
        '一直以来bangumi陷入了一个重大误区，火不是作品的负面buff，理应是作品荣誉的勋章。

火无论在作品商业成绩还是口碑成绩都是最重要的标准，你的销量和播放量离不开火，你的评分人数和讨论度离不开火……',
        'https://bgm.tv/subject/topic/37616', 1, '', 1, 2310, 5, '2026-01-10 12:00:00'),
       (3, 2, '迟到的追番完结有感',
        '明明最后一集刚出就看完了，现在才有时间写哈哈。

-评分9分，一集一集追下来的，中间有一集质量下滑很大（比如比目鱼），不过虽然不是每集都像第一集一样顶但是也够强了。
第一个方面，制作水准是无可争议的，就这么优秀的制作，没有一帧画面背景不是在动的，真的太有经费了，色调看着也让人很舒服，人物动作也很流畅表情也很生动，赞的很。',
        'https://bgm.tv/blog/346005', 1, '', 0, 860, 2, '2026-02-01 22:15:00'),
       (4, 2, '求推荐一些冷门佳作（待审核示例）',
        '最近补番有些迷茫，想看一些人气不高但是质量很顶的作品，各位大佬有什么推荐吗？

比如像《白箱》《花开伊吕波》这种不火但制作精良的。',
        '', 0, '', 0, 0, 0, '2026-08-25 09:00:00'),
       (5, 4, '一条被驳回的帖子（示例）',
        '这条帖子因为内容违规被管理员驳回了，示例演示用。',
        '', 2, '内容与本站主题无关，请围绕动画内容发帖', 0, 0, 0, '2026-08-24 15:30:00');

-- =============================================================
-- 4. 回复表 forum_reply
-- =============================================================
DROP TABLE IF EXISTS forum_reply;
CREATE TABLE forum_reply
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    post_id     BIGINT        NOT NULL COMMENT '帖子ID',
    user_id     BIGINT        NOT NULL COMMENT '回复人ID',
    content     VARCHAR(1000) NOT NULL COMMENT '回复内容',
    deleted     TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0未删 1已删',
    create_time DATETIME               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_post_id (post_id),
    KEY idx_user_id (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='回复表';

-- 种子数据（与前端 Mock db.js 一致，ID 对齐）
INSERT INTO forum_reply (id, post_id, user_id, content, create_time)
VALUES (1, 1, 2, '冰菓真的值得多刷，每次看都有新感受！', '2026-01-06 08:00:00'),
       (2, 1, 3, '京阿尼的演出和米泽的推理确实是绝配。', '2026-01-06 10:20:00'),
       (3, 1, 4, '补番清单+1，今晚就去看。', '2026-01-07 21:40:00'),
       (4, 2, 2, '有道理，但我觉得小火的作品质量也很高，不能一概而论。', '2026-01-11 09:00:00'),
       (5, 2, 1, '讨论度也是作品生命力的一部分嘛。', '2026-01-11 13:30:00'),
       (6, 3, 1, '这季度确实经费拉满，制作没得挑。', '2026-02-02 11:00:00');