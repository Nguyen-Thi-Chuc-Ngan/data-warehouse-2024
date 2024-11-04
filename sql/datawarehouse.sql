/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : localhost:3306
 Source Schema         : datawarehouse

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 04/11/2024 22:06:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for color_dim
-- ----------------------------
DROP TABLE IF EXISTS `color_dim`;
CREATE TABLE `color_dim`  (
  `color_id` int NOT NULL AUTO_INCREMENT,
  `color_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`color_id`) USING BTREE,
  UNIQUE INDEX `color_name`(`color_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of color_dim
-- ----------------------------
INSERT INTO `color_dim` VALUES (1, '');
INSERT INTO `color_dim` VALUES (4, 'black');
INSERT INTO `color_dim` VALUES (2, 'đen');
INSERT INTO `color_dim` VALUES (6, 'kem');
INSERT INTO `color_dim` VALUES (3, 'nâu');
INSERT INTO `color_dim` VALUES (5, 'xanh');

-- ----------------------------
-- Table structure for product_color_bridge
-- ----------------------------
DROP TABLE IF EXISTS `product_color_bridge`;
CREATE TABLE `product_color_bridge`  (
  `product_id` bigint NOT NULL,
  `color_id` int NOT NULL,
  PRIMARY KEY (`product_id`, `color_id`) USING BTREE,
  INDEX `color_id`(`color_id` ASC) USING BTREE,
  CONSTRAINT `product_color_bridge_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product_dim` (`product_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `product_color_bridge_ibfk_2` FOREIGN KEY (`color_id`) REFERENCES `color_dim` (`color_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_color_bridge
-- ----------------------------
INSERT INTO `product_color_bridge` VALUES (1999679, 1);
INSERT INTO `product_color_bridge` VALUES (6579831, 1);
INSERT INTO `product_color_bridge` VALUES (26667448, 1);
INSERT INTO `product_color_bridge` VALUES (76368202, 1);
INSERT INTO `product_color_bridge` VALUES (171202214, 1);
INSERT INTO `product_color_bridge` VALUES (190321339, 1);
INSERT INTO `product_color_bridge` VALUES (207001017, 1);
INSERT INTO `product_color_bridge` VALUES (210129600, 1);
INSERT INTO `product_color_bridge` VALUES (254124101, 1);
INSERT INTO `product_color_bridge` VALUES (274711495, 1);
INSERT INTO `product_color_bridge` VALUES (274903488, 1);
INSERT INTO `product_color_bridge` VALUES (275262532, 1);
INSERT INTO `product_color_bridge` VALUES (275786273, 1);
INSERT INTO `product_color_bridge` VALUES (275837587, 1);
INSERT INTO `product_color_bridge` VALUES (7976948, 2);
INSERT INTO `product_color_bridge` VALUES (75990118, 2);
INSERT INTO `product_color_bridge` VALUES (262680091, 2);
INSERT INTO `product_color_bridge` VALUES (271426057, 2);
INSERT INTO `product_color_bridge` VALUES (7976948, 3);
INSERT INTO `product_color_bridge` VALUES (262680091, 3);
INSERT INTO `product_color_bridge` VALUES (271426057, 3);
INSERT INTO `product_color_bridge` VALUES (138492571, 4);
INSERT INTO `product_color_bridge` VALUES (274948039, 5);
INSERT INTO `product_color_bridge` VALUES (274948039, 6);

-- ----------------------------
-- Table structure for product_dim
-- ----------------------------
DROP TABLE IF EXISTS `product_dim`;
CREATE TABLE `product_dim`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` bigint NULL DEFAULT NULL,
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `product_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `price` decimal(15, 2) NULL DEFAULT NULL,
  `original_price` decimal(15, 2) NULL DEFAULT NULL,
  `discount` decimal(15, 2) NULL DEFAULT NULL,
  `rating_average` decimal(3, 2) NULL DEFAULT NULL,
  `review_count` int NULL DEFAULT NULL,
  `discount_rate` decimal(5, 2) NULL DEFAULT NULL,
  `quantity_sold` int NULL DEFAULT NULL,
  `image_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `product_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `thumbnail_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `url_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `url_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `short_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `short_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `dt_ex` date NULL DEFAULT '9999-12-31',
  `isDelete` bit(1) NULL DEFAULT b'0',
  `delete_time` timestamp NULL DEFAULT NULL,
  `action_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_dim
-- ----------------------------
INSERT INTO `product_dim` VALUES (1, 1999679, '4138201894640', 'Giày Tây Nam Vân Cá Sấu Huy Hoàng HT7128 - Đen', 'Huy Hoàng', 545400.00, 909000.00, 363600.00, 4.00, 1, 40.00, 2, 'https://salt.tikicdn.com/cache/w1200/ts/product/05/e5/53/7f80b7746e87032c7a52c4b555e1c275.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/e0/43/c2/7e14583cf502623041c2649a9e9bd38a.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/53/21/0a/db05d029ab9f696ce4f254839ab7d406.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/d0/03/07/d2a3a5798033f21afa508e26fe9755de.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/05/e5/53/7f80b7746e87032c7a52c4b555e1c275.jpg', 'giay-tay-nam-van-ca-sau-huy-hoang-ht7128-den-p1999679', 'giay-tay-nam-van-ca-sau-huy-hoang-ht7128-den-p1999679.html?spid=45520906', 'https://tiki.vn/product-p1999679.html?spid=45520906', 'Giày Tây Nam Vân Cá Sấu Huy Hoàng HT7128 với chất liệu da bò bền đẹp kết hợp đế êm nhẹ chống trơn trượt giúp bạn nam luôn thoải mái, tự tin khi đi chơi, dạo phố, đi làm nơi công sở hay đi dự tiệc.Gi...', '2024-10-27 16:06:35', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (2, 6579831, '4185497177827', 'Dép Nam Bảng Ngang BIGGBEN Da Bò Thật DN61', 'BIGGBEN', 309000.00, 350000.00, 41000.00, 4.60, 96, 12.00, 348, 'https://salt.tikicdn.com/cache/w1200/ts/product/95/78/35/a6af99067205c5d395aa1e95322a3376.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/94/5a/97/67125682ea45e15da7962871b96717a5.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/9f/3d/1f/b08892d7c0356abc07c85e40c505026f.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/07/3f/10/c6679dcf6f6e985b39d5eed0f13d2266.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/95/78/35/a6af99067205c5d395aa1e95322a3376.jpg', 'dep-nam-bang-ngang-biggben-da-bo-that-dn61-p6579831', 'dep-nam-bang-ngang-biggben-da-bo-that-dn61-p6579831.html?spid=6579851', 'https://tiki.vn/product-p6579831.html?spid=6579851', 'Dép Nam với chất liệu da bò bền đẹp kết hợp đế cao su êm nhẹ chống trơn trượt giúp bạn Nam luôn thoải mái, tự tin khi đi chơi, dạo phố, đi làm nơi công sở hay đi dự tiệc.Dép nam được xử lý đặc biệt đ...', '2024-10-27 16:06:25', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (3, 7976948, '8031505488293', 'Giày Mọi Nam Da Bò Sunpolo SU011', 'SUNPOLO', 990000.00, 990000.00, 0.00, 4.70, 19, 0.00, 59, 'https://salt.tikicdn.com/cache/w1200/ts/product/85/01/51/ecae59c57dcab422e3bd6de378688257.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/f1/d0/6d/85679400ee077765b76faa2e4898e89f.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/46/6c/b9/c74284e64a0b39f6aaac4a3b6a1391ac.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/50/81/ec/115d1b5478b18731d3510255bb90366d.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/bf/d9/0a/3bf56aebfabd39bfb5e6d8cf59950e98.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/ca/70/74/585214272cd96ce5d5b24cb9dd18b1ef.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/80/8b/54/9462dfea4e547be7e5ba196c3057afad.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/85/01/51/ecae59c57dcab422e3bd6de378688257.jpg', 'giay-moi-nam-da-bo-sunpolo-su011-p7976948', 'giay-moi-nam-da-bo-sunpolo-su011-p7976948.html?spid=7986524', 'https://tiki.vn/product-p7976948.html?spid=7986524', 'Giày Mọi Nam Da Bò Sunpolo SU011 được may từ chất liệu da bò cao cấp mềm mịn, bền đẹp.Đế cao su rãnh chống trơn trượt hiệu quả.Thiết kế đơn giản, khỏe khoắn, đường may tỉ mỉ, thoải mái khi mang. L...', '2024-10-27 16:06:12', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (4, 26667448, '6574075015565', 'Dép Nam BIGGBEN Da Bò Thật Cao Cấp DN197', 'BIGGBEN', 289000.00, 350000.00, 61000.00, 4.60, 9, 17.00, 26, 'https://salt.tikicdn.com/cache/w1200/ts/product/ad/9a/fa/7fe18fcb8e5afd33cf7a36538050c627.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/58/b5/98/198e5f05863a36d35950cbea4a2c2af9.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/77/07/01/caeb7a8f3d5a72dd8529ebc778fbe2ed.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/ac/3b/a6/c36837193bc3f44ad4be492d2051b6a4.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/ad/9a/fa/7fe18fcb8e5afd33cf7a36538050c627.jpg', 'dep-nam-biggben-da-bo-that-cao-cap-dn197-p26667448', 'dep-nam-biggben-da-bo-that-cao-cap-dn197-p26667448.html?spid=26667456', 'https://tiki.vn/product-p26667448.html?spid=26667456', 'Dép Nam với chất liệu da bò bền đẹp kết hợp đế cao su êm nhẹ chống trơn trượt giúp bạn Nam luôn thoải mái, tự tin khi đi chơi, dạo phố, đi làm nơi công sở hay đi dự tiệc.Dép Nam được xử lý đặc biệt ...', '2024-10-27 16:06:17', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (5, 75990118, '2496908134776', 'Dép nam đế cao 3.5cm da bò thật may viền thủ công chắc chắn thời trang cao cấp chính hãng Trường Hải DC150', 'Trường Hải', 310000.00, 359000.00, 49000.00, 4.30, 17, 14.00, 41, 'https://salt.tikicdn.com/cache/w1200/ts/product/69/8a/07/7085b4acedf7eb4be1e16f0a1e986f04.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/8f/18/6f/02cc7042f923d47058ad4d563f1f752e.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/27/ad/ce/4ec202d0b7260b1e0ced6ca79db9f417.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/e7/c7/d5/5c2db7d6ce8cd48901fc3dc7b7e7b2b7.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/b0/bc/2a/434800e8b1b980f37b6cf4d80e2274c4.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/69/8a/07/7085b4acedf7eb4be1e16f0a1e986f04.jpg', 'dep-nam-de-cao-3-5cm-da-bo-that-may-vien-thu-cong-chac-chan-thoi-trang-cao-cap-chinh-hang-truong-hai-dc150-p75990118', 'dep-nam-de-cao-3-5cm-da-bo-that-may-vien-thu-cong-chac-chan-thoi-trang-cao-cap-chinh-hang-truong-hai-dc150-p75990118.html?spid=75990130', 'https://tiki.vn/product-p75990118.html?spid=75990130', 'Thông tin chi tiết sản phẩm :Màu :Nâu, đen, vàngChất liệu bên ngoài: da bò cao cấpXuất xứ : Việt namChất liệu đế: kếp mềm không trơn Chiều cao: 3.5cmChất liệu bên trong: da Size: 38-39-40-41-4...', '2024-10-27 16:06:21', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (6, 76368202, '6014605699402', 'Dép nam da bò BIGGBEN cao cấp DN272', 'BIGGBEN', 339000.00, 450000.00, 111000.00, 4.50, 17, 25.00, 49, 'https://salt.tikicdn.com/cache/w1200/ts/product/bc/e4/7a/2a25199a39e207bc14693c05e559804e.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/62/26/43/cd1a43b1bde46827bd046030ececd4a5.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/74/fb/73/c9e4f2a7e76b49a0c43416bc7d94781b.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/9c/fe/6b/6228093d0d9e1bd3222f8816db65455b.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/f5/70/86/501ff579c2c321ff512a6d1e35c2b8c5.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/1a/54/7c/f0794436e365322ac9ef0f93761e2d15.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/bc/e4/7a/2a25199a39e207bc14693c05e559804e.jpg', 'dep-nam-da-bo-biggben-cao-cap-dn272-p76368202', 'dep-nam-da-bo-biggben-cao-cap-dn272-p76368202.html?spid=76368204', 'https://tiki.vn/product-p76368202.html?spid=76368204', 'Dép Nam với chất liệu da Bò bền đẹp kết hợp đế cao su êm nhẹ chống trơn trượt giúp bạn Nam luôn thoải mái, tự tin khi đi chơi, dạo phố, đi làm nơi công sở hay đi dự tiệc.Dép Nam được xử lý đặc biệt ...', '2024-10-27 16:06:01', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (7, 138492571, '4840764712791', 'Giày Da Pierre Cardin - PCMFWL 732', 'Piierre Cardin', 1190000.00, 2990000.00, 1800000.00, 4.60, 73, 60.00, 269, 'https://salt.tikicdn.com/cache/w1200/ts/product/78/87/d5/a836956e7fd4ed2d20d1e9bd1e2de524.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/ae/fd/57/0ff795c44e68d2cebc602e7efeee2c23.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/cd/ff/d2/429c6fa2831bb334a75582b183707a75.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/dd/f5/69/98d03d4dab90c540c5bf260cc203c470.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/5e/89/26/a8988dfb29e442ea9a8eb438f0a9e3a6.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/ba/bf/8b/10f1b61da4a8e52c7b65f35a980ca5a2.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/db/dd/4c/2c327eca635bca67196043cb2c99617b.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/78/87/d5/a836956e7fd4ed2d20d1e9bd1e2de524.jpg', 'giay-da-pierre-cardin-pcmfwl732-p138492571', 'giay-da-pierre-cardin-pcmfwl732-p138492571.html?spid=138492573', 'https://tiki.vn/product-p138492571.html?spid=138492573', ' Sản phẩm: Giày Da - PCMFWLF 732 Thiết kế: Giày tây có buộc dây sang trọng. Không hoạ tiết cầu kỳ cùng chất liệu đen đơn sắc giúp bạn dễ dàng mix-match với nhiều trang phục khác nhau. Blank Derby s...', '2024-10-27 16:05:23', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (8, 171202214, '8766487879697', 'Dép quai ngang nam Giavy U6909', 'GIAVY', 235000.00, 235000.00, 0.00, 5.00, 1, 0.00, 2, 'https://salt.tikicdn.com/cache/w1200/ts/product/2c/eb/2c/e17054c4a0383ec22404020326c87426.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/58/33/04/bf48f2334fb7d6efaffd72d75368a96d.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/27/7f/cd/0c9f4d95d05b2cc2ef949360a2c80615.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/d9/3a/d7/f081ce0a81fe9066f128138d8efb5dcb.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/92/40/5e/9f192b9c92baed963fdb4cea465fba1f.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/f2/4f/33/da7542c726ea6f665465c71278868548.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/97/8e/ec/e9d032971da95f1bec5b74a42f3cd1a2.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/65/74/01/a1511b23d02c70ae905d697dc58a915d.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/43/30/44/aa75c79bc6594ccfe14a26a7f6939dc2.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/2c/eb/2c/e17054c4a0383ec22404020326c87426.jpg', 'dep-quai-ngang-nam-giavy-u6909-p171202214', 'dep-quai-ngang-nam-giavy-u6909-p171202214.html?spid=171202220', 'https://tiki.vn/product-p171202214.html?spid=171202220', '  DÉP NAM   Thương hiệu độc quyền : GIAVY.   Xuất xứ: Việt Nam.   Mã sản phẩm: U6909.   Kiểu dáng: 2 quai ngang, đế bệt .   Chất liệu quai dù mềm êm chân, đường may tỉ mỉ.   Đế cao su, có r...', '2024-10-27 16:05:34', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (9, 190321339, '9117675237275', 'Dép Nam Quai Ngang Đúc Liền Hot Trend DP14', 'Laceva', 79000.00, 79000.00, 0.00, 4.50, 18, 0.00, 88, 'https://salt.tikicdn.com/cache/w1200/ts/product/d8/66/79/f7010f462ae8d9ab97d66a66d433ac81.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/16/76/51/b1840ae93770cf6e055f4218ac092910.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/ed/84/b9/2736836d44af613beb6217d6058cba64.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/3c/b9/d6/873cf72a97adac108279f6e796ebce01.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/78/57/d5/853b602b539a1ed52a3cc5d2489094ee.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/f0/2c/fd/9ab83edba42df8ab2d623b0ef721c08f.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/d8/66/79/f7010f462ae8d9ab97d66a66d433ac81.jpg', 'de-p-nam-quai-ngang-du-c-lie-n-hot-trend-dp14-p190321339', 'de-p-nam-quai-ngang-du-c-lie-n-hot-trend-dp14-p190321339.html?spid=190321357', 'https://tiki.vn/product-p190321339.html?spid=190321357', '* CÁCH MUA NHIỀU SẢN PHẨM VỚI SIZE VÀ MÀU KHÁC NHAU.-- Các bạn chọn màu và size, rồi thêm vào giỏ hàng.-- Sau đó chọn màu và size tiếp theo rồi cho vào giỏ hàng ( Như đi siêu...', '2024-10-27 16:06:44', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (10, 207001017, '8725162387122', 'Dép tông lào Thái Lan nam siêu nhẹ MIXSTAR  MV4 siêu bền, nhẹ, đi mưa không ngấm nước', 'Mixstar', 60000.00, 60000.00, 0.00, 5.00, 1, 0.00, 6, 'https://salt.tikicdn.com/cache/w1200/ts/product/e5/21/61/24916c4fc409494f553c839086b0aa1a.png;https://salt.tikicdn.com/cache/w1200/ts/product/66/4c/d3/ac7d4d5930e3ea60c0525e62a0a2a88a.png;https://salt.tikicdn.com/cache/w1200/ts/product/c2/61/c8/8be9e13defcf1b348880143249634581.png;https://salt.tikicdn.com/cache/w1200/ts/product/7c/a3/30/a2091a20c792a51c07274a9191f7d202.png;https://salt.tikicdn.com/cache/w1200/ts/product/9f/91/1d/b7a9a00ae93e7775045b9e6cc90acaed.png;https://salt.tikicdn.com/cache/w1200/ts/product/8e/a4/df/a195593afaabc8f3a123b0958bddf3d9.png', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/e5/21/61/24916c4fc409494f553c839086b0aa1a.png', 'dep-tong-lao-thai-lan-nam-sieu-nhe-mixstar-mv4-sieu-ben-nhe-di-mua-khong-ngam-nuoc-p207001017', 'dep-tong-lao-thai-lan-nam-sieu-nhe-mixstar-mv4-sieu-ben-nhe-di-mua-khong-ngam-nuoc-p207001017.html?spid=207001031', 'https://tiki.vn/product-p207001017.html?spid=207001031', 'Dép tông lào Thái Lan nam siêu nhẹ MIXSTAR MV4 siêu bền, nhẹ, đi mưa không ngấm nướcDép Thái Lan đi biển GIỚI THIỆU SẢN PHẨM • Made in Thailand – Thương hiệu: MIXSTAR MV4• Chất liệu: đế xốp siêu nhẹ, ...', '2024-10-27 16:05:48', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (11, 210129600, '9736226009717', 'Chai xịt khử mùi giày dép NaNo Bạc, xịt khử mùi hôi chân, xịt giày khử mùi khử vi khuẩn 99% loại bỏ mùi hôi giày dép - Hàng chính hãng', 'FuNu', 22000.00, 45000.00, 23000.00, 4.70, 152, 51.00, 1601, 'https://salt.tikicdn.com/cache/w1200/ts/product/68/b5/a1/e2d27cac34740ceaf6ffdf15d2a3b9f5.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/68/b5/a1/e2d27cac34740ceaf6ffdf15d2a3b9f5.jpg', 'chai-xit-khu-mui-giay-dep-nano-bac-xit-khu-mui-hoi-chan-xit-giay-khu-mui-khu-vi-khuan-99-loai-bo-mui-hoi-giay-dep-p210129600', 'chai-xit-khu-mui-giay-dep-nano-bac-xit-khu-mui-hoi-chan-xit-giay-khu-mui-khu-vi-khuan-99-loai-bo-mui-hoi-giay-dep-p210129600.html?spid=274544729', 'https://tiki.vn/product-p210129600.html?spid=274544729', 'Xịt Khử Mùi Hôi Giày Dép Tất Vớ Tủ Giày Nano Bạc Công Nghệ Nhật Bản Khử Mùi Hôi Chân Hiệu QuảXịt Khử Mùi Hôi Giày Dép Tất Vớ Tủ Giày Nano Bạc Công Nghệ Nhật Bản đánh bay nỗi lo lắng phiền muộn ...', '2024-10-27 16:06:06', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (12, 254124101, '4690695999149', 'Dép nam trung niên bền đẹp thương hiệu KEEDO KDN09', 'KEEDO', 340000.00, 340000.00, 0.00, 5.00, 1, 0.00, 0, 'https://salt.tikicdn.com/cache/w1200/media/catalog/product/f5/15/2228f38cf84d1b8451bb49e2c4537081.png', 'configurable', '', 'dep-nam-trung-nien-ben-dep-thuong-hieu-keedo-kdn09-p254124101', 'dep-nam-trung-nien-ben-dep-thuong-hieu-keedo-kdn09-p254124101.html', 'https://tiki.vn/product-p254124101.html', 'Dép quai ngang nam KEEDO KDN09Mã Sản Phẩm: KDN09- Màu Sắc: Đen, Nâu- Chất Liệu: Đế PU siêu nhẹ, quai da tổng hợp- Xuất xứ: Việt Nam*Dép nam quai ngang mẫu cơ bản bền đẹp-------------------------------...', '2024-10-27 16:05:27', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (13, 262680091, '9199086087578', 'Giày tây nam có dây Pierre Cardin PCMFWL 766, đế da cao cấp, logo đính nổi bật, mang chân êm ái ', 'Pierre Cardin', 1190000.00, 2990000.00, 1800000.00, 4.60, 5, 60.00, 25, 'https://salt.tikicdn.com/cache/w1200/ts/product/19/c3/6b/19fce3ee481f67fb1d78453aa2c82987.png;https://salt.tikicdn.com/cache/w1200/ts/product/f1/61/8e/6b0ffd0938175c96b21db23e9c4c0270.png;https://salt.tikicdn.com/cache/w1200/ts/product/04/84/8a/d6932b3283344d258a91cbd88a283c70.png;https://salt.tikicdn.com/cache/w1200/ts/product/c4/45/54/ddeea96a57dafd6158ca0e8a078aa784.png', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/19/c3/6b/19fce3ee481f67fb1d78453aa2c82987.png', 'giay-tay-nam-co-day-pierre-cardin-766-p262680091', 'giay-tay-nam-co-day-pierre-cardin-766-p262680091.html?spid=262680104', 'https://tiki.vn/product-p262680091.html?spid=262680104', 'Sản phẩm: Giày Derby Pierre Cardin - PCMFWLE 7661. Thông Tin Chi Tiết Sản Phẩm thuộc dòng Giày Tây cổ điển. Thiết kế giày đơn giản nhưng vô cùng sang trọng nhờ vào chất liệu da bò cao cấp, nhập khẩu...', '2024-10-27 16:05:43', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (14, 271426057, '7267079160489', 'Giày Sandal Nam BIGGBEN Da Bò Thật Cao Cấp SD135', 'BIGGBEN', 349000.00, 450000.00, 101000.00, 0.00, 0, 22.00, 2, 'https://salt.tikicdn.com/cache/w1200/ts/product/4e/11/7a/d80b7dd1dba39a905e3749ea6f75bd21.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/72/e0/e2/f0d91975aa53c41d2e0471d20abaa86b.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/f3/97/04/52adc8503620f3c095a9ec6e5655fbb6.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/35/b2/84/0940d929557f4a04ea24f7b32b5ce8af.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/27/05/f9/2e30984b1ca4dc7758e12f0d78f4b171.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/4e/11/7a/d80b7dd1dba39a905e3749ea6f75bd21.jpg', 'giay-sandal-nam-biggben-da-bo-that-cao-cap-sd135-p271426057', 'giay-sandal-nam-biggben-da-bo-that-cao-cap-sd135-p271426057.html?spid=271426063', 'https://tiki.vn/product-p271426057.html?spid=271426063', 'Sandal Nam với chất liệu da Bò bền đẹp kết hợp đế cao su êm nhẹ chống trơn trượt giúp bạn Nam luôn thoải mái, tự tin khi đi chơi, dạo phố, đi làm.Sandal Nam được xử lý đặc biệt để tạo độ bóng mờ ...', '2024-10-27 16:05:38', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (15, 274711495, '7741840852260', 'Giày Sandal nam chống kiểu dáng mới chống trơn, trượt – GSD9089', 'Bee Gee', 403000.00, 840000.00, 437000.00, 4.00, 2, 52.00, 6, 'https://salt.tikicdn.com/cache/w1200/ts/product/b8/e5/31/ca5aa251c70809dc64213cf21b3ccf59.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/67/b5/3f/a53cdd997726d8243037b55be34fa9f6.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/16/2f/9f/f33c210e5211f5fa3e135e6e218652d7.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/f0/f5/65/5cadc28c6bc46dc0a9b937afb3e3dc8d.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/03/42/54/9f7653c85f890bbb75bfabdaa62c204c.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/ae/21/24/a9e6a9cb1d6a82f006ea3d257608afeb.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/67/97/eb/a2da03c10be7d79cd1b2292b7e143744.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/e1/d3/24/8521cf7792d82d75a1a639cb8300e1ab.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/8b/00/a0/838fd91c9ba302250f74c13efa51d8f5.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/19/25/12/b61fe4d44590ca8742ba6b08eb3530d5.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/4d/da/d4/1b93d0fd33e050cc63ce5d5d433ae2ea.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/b8/e5/31/ca5aa251c70809dc64213cf21b3ccf59.jpg', 'giay-sandal-nam-chong-kieu-dang-moi-tron-truot-gsd9089-p274711495', 'giay-sandal-nam-chong-kieu-dang-moi-tron-truot-gsd9089-p274711495.html?spid=274711503', 'https://tiki.vn/product-p274711495.html?spid=274711503', 'Thiết kế: có form giày dễ thương, năng động, cá tính2.Công nghệ đế: Giày ép đế3.Chất liệu đế: Cao su4.Giới tính áp dụng: Nam/ Teen5.Nhóm Tuổi áp dụng: Teen, người lớn6.Phong cách: Hàn Quốc7.Size...', '2024-10-27 16:06:40', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (16, 274903488, '2165833565548', 'Sandal Nam Biti\'s Hunter HEM000800 Size (39-44)', 'Biti\'s', 502000.00, 529000.00, 27000.00, 5.00, 4, 5.00, 7, 'https://salt.tikicdn.com/cache/w1200/ts/product/74/8a/c1/7133c53fa436216507e6c147869e042f.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/ca/56/51/ce8c896c0829cd9b3e5d907f1d8b2430.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/7b/6e/2d/1512633b6628cb11881b0def4425dee5.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/a5/a5/d3/a269d05dcb85a76305b5ad28f44c8b62.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/c3/4c/e7/bdf089dbec832791aeeb95228134cfd5.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/79/22/b9/9c8c8935fb68e7ac0e75969402be9199.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/74/8a/c1/7133c53fa436216507e6c147869e042f.jpg', 'sandal-nam-biti-s-hunter-hem000800-size-39-44-p274903488', 'sandal-nam-biti-s-hunter-hem000800-size-39-44-p274903488.html?spid=274903518', 'https://tiki.vn/product-p274903488.html?spid=274903518', 'Sandal Eva Phun Hunter Nam   ● Kích thước: -39-40-41-42-43-44● Màu sắc: Đen - Nâu - Xám● Đế giàySanda có đế Eva Phun (Injection Phylon) cao su nhẹ nên mang lại sự nhẹ nhàng, êm ái cho bàn chân....', '2024-10-27 16:06:48', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (17, 274948039, '3450681150052', 'Giày thể thao nam đế êm nhẹ, thoáng khí đế cao su đúc, chống trơn trượt hạn chế mòn – BEE GEE - GNA2019', 'Bee Gee', 336000.00, 700000.00, 364000.00, 4.00, 1, 52.00, 5, 'https://salt.tikicdn.com/cache/w1200/ts/product/b2/c9/18/df7c5312bf3ed7c62cc51675cfd9e88b.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/f9/3b/65/9e6d9daadb89b5db0628e47778d5395d.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/4d/72/d1/49729e410ce48c16321f9f8cc2bdd5da.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/58/76/f4/89fbbd5df2cb0ae7b1f8c06c9c7552df.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/af/83/94/56859b5fbb525cc5b6f7a48d682d4d86.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/1e/2e/c5/640e85f7c730abe40754f55dcd9edae3.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/80/aa/15/7caf6861a7017392d62186aa69906656.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/6e/1e/9b/cd58ab3db09a8792af352f8214e87783.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/1b/59/b8/a2079f2a276468f7335f486e196bb5e8.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/b2/c9/18/df7c5312bf3ed7c62cc51675cfd9e88b.jpg', 'giay-the-thao-nam-de-em-nhe-thoang-khi-de-cao-su-duc-chong-tron-truot-han-che-mon-bee-gee-gna2019-p274948039', 'giay-the-thao-nam-de-em-nhe-thoang-khi-de-cao-su-duc-chong-tron-truot-han-che-mon-bee-gee-gna2019-p274948039.html?spid=274948041', 'https://tiki.vn/product-p274948039.html?spid=274948041', 'Giày thể thao nam thời trang, giày chạy bộ nam ôm chân thoáng khíGiày được thiết kế đẹp mắt, có tính thẩm mỹ caoThiết kế da cao cấp mềm, thoáng khíMũi giày ôm theo dáng bàn chân, không gây khó chịu...', '2024-10-27 16:06:31', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (18, 275262532, '6942420299987', 'Giày Sandal Nam - Chất Liệu Nhựa EVA Mềm, Nhẹ, Êm Chân, Thoải Mái, Chống Trơn Trượt', 'VAC ', 135000.00, 165000.00, 30000.00, 0.00, 0, 18.00, 12, 'https://salt.tikicdn.com/cache/w1200/ts/product/64/20/d7/1b8a67c16a789eecf1c4aa2274518a9f.png', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/64/20/d7/1b8a67c16a789eecf1c4aa2274518a9f.png', 'giay-sandal-nam-chat-lieu-nhua-eva-mem-nhe-em-chan-thoai-mai-chong-tron-truot-p275262532', 'giay-sandal-nam-chat-lieu-nhua-eva-mem-nhe-em-chan-thoai-mai-chong-tron-truot-p275262532.html?spid=275262536', 'https://tiki.vn/product-p275262532.html?spid=275262536', 'Giày sandal được sản xuất từ nhựa E.V.A  nguồn gốc Taiwan, Korea (Nói không với Trung Quốc), cực kỳ thân thiện với môi trường, không chứa chất độc hại, an toàn sử dụng với cả trẻ em.Giày được thiết k...', '2024-10-27 16:05:52', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (19, 275786273, '6754701456909', 'Dép Evaphun Nam Biti\'s BEM001400 (Size 38-44)', 'Biti\'s', 188000.00, 198000.00, 10000.00, 0.00, 0, 5.00, 3, 'https://salt.tikicdn.com/cache/w1200/ts/product/85/24/69/f79037dfcf256597d6a9d69cf47d0b48.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/5d/97/52/87137dd4c55f62d1b771dd92c5cc61a3.png;https://salt.tikicdn.com/cache/w1200/ts/product/47/2c/fb/e106d0daeb6563e943a0a0b47ed7c5c4.png;https://salt.tikicdn.com/cache/w1200/ts/product/0d/ee/bf/79daf8f9523d6dfdcb9a0f2a0b56b5ad.png;https://salt.tikicdn.com/cache/w1200/ts/product/07/ec/2c/9a19a9fa2c83b614e5fc6b347ae5f3ca.png;https://salt.tikicdn.com/cache/w1200/ts/product/a7/08/71/22f303bf60a0c42751637b90a83f2d6b.png;https://salt.tikicdn.com/cache/w1200/ts/product/29/d4/06/9bd89104b8ad745cb3b959b6c7d4cf12.png;https://salt.tikicdn.com/cache/w1200/ts/product/0d/95/07/49268f09b829f718a5bd79f075e98afb.png', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/85/24/69/f79037dfcf256597d6a9d69cf47d0b48.jpg', 'dep-evaphun-nam-biti-s-bem001400-size-38-44-p275786273', 'dep-evaphun-nam-biti-s-bem001400-size-38-44-p275786273.html?spid=275786281', 'https://tiki.vn/product-p275786273.html?spid=275786281', 'MÔ TẢ SẢN PHẨM :- Đế cực êm, đi đứng lâu vẫn thoải mái - tăng chiều cao ( đế cao 3cm ) - Đế được làm từ chất liệu nhựa  giúp dép thêm bền đẹp, form ôm chân và thoải mái khi di chuyển. - Phù hợp để...', '2024-10-27 16:05:57', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');
INSERT INTO `product_dim` VALUES (20, 275837587, '9817468043890', 'Sandal Bitis nam (38-44)', 'Biti\'s', 323000.00, 340000.00, 17000.00, 0.00, 0, 5.00, 1, 'https://salt.tikicdn.com/cache/w1200/ts/product/2b/dc/6d/822e4b55a5793287b618893709ade288.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/89/d2/04/bd6e9d12549830d246b96f8f0bbf765e.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/52/3f/48/eef3a5487dc730169104077f16b896b0.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/37/35/90/66bdbbe4e5cd0c5da21fca13431673c1.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/db/c6/ca/4c8e1c6b6dfd1a8026386bba4f072697.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/31/ef/37/77a1d73ae6bce9438368c25c55ff331d.jpg;https://salt.tikicdn.com/cache/w1200/ts/product/b2/30/5a/a1b97ea59c4a8b1731f50dee79f51c18.jpg', 'configurable', 'https://salt.tikicdn.com/cache/280x280/ts/product/2b/dc/6d/822e4b55a5793287b618893709ade288.jpg', 'sandal-bitis-nam-38-44-p275837587', 'sandal-bitis-nam-38-44-p275837587.html?spid=275837597', 'https://tiki.vn/product-p275837587.html?spid=275837597', 'Mẫu Sandal  với thiết kế quai tiện lợi Được làm từ chất liệu da , bên cạnh đó, với đường may tinh tế, phần mặt đế cao su thiết kế dạng rãnh chống trơn trượt, tạo cảm giác êm chân và chắc chắn kh...', '2024-10-27 16:05:30', '9999-12-31', b'0', NULL, '2024-10-29 20:17:07');

-- ----------------------------
-- Table structure for product_size_bridge
-- ----------------------------
DROP TABLE IF EXISTS `product_size_bridge`;
CREATE TABLE `product_size_bridge`  (
  `product_id` bigint NOT NULL,
  `size_id` int NOT NULL,
  PRIMARY KEY (`product_id`, `size_id`) USING BTREE,
  INDEX `size_id`(`size_id` ASC) USING BTREE,
  CONSTRAINT `product_size_bridge_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product_dim` (`product_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `product_size_bridge_ibfk_2` FOREIGN KEY (`size_id`) REFERENCES `size_dim` (`size_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_size_bridge
-- ----------------------------
INSERT INTO `product_size_bridge` VALUES (1999679, 1);
INSERT INTO `product_size_bridge` VALUES (6579831, 1);
INSERT INTO `product_size_bridge` VALUES (26667448, 1);
INSERT INTO `product_size_bridge` VALUES (75990118, 1);
INSERT INTO `product_size_bridge` VALUES (76368202, 1);
INSERT INTO `product_size_bridge` VALUES (138492571, 1);
INSERT INTO `product_size_bridge` VALUES (171202214, 1);
INSERT INTO `product_size_bridge` VALUES (207001017, 1);
INSERT INTO `product_size_bridge` VALUES (210129600, 1);
INSERT INTO `product_size_bridge` VALUES (254124101, 1);
INSERT INTO `product_size_bridge` VALUES (271426057, 1);
INSERT INTO `product_size_bridge` VALUES (274711495, 1);
INSERT INTO `product_size_bridge` VALUES (274903488, 1);
INSERT INTO `product_size_bridge` VALUES (274948039, 1);
INSERT INTO `product_size_bridge` VALUES (275786273, 1);
INSERT INTO `product_size_bridge` VALUES (7976948, 2);
INSERT INTO `product_size_bridge` VALUES (7976948, 3);
INSERT INTO `product_size_bridge` VALUES (7976948, 4);
INSERT INTO `product_size_bridge` VALUES (7976948, 5);
INSERT INTO `product_size_bridge` VALUES (262680091, 5);
INSERT INTO `product_size_bridge` VALUES (275837587, 5);
INSERT INTO `product_size_bridge` VALUES (7976948, 6);
INSERT INTO `product_size_bridge` VALUES (262680091, 6);
INSERT INTO `product_size_bridge` VALUES (275837587, 6);
INSERT INTO `product_size_bridge` VALUES (7976948, 7);
INSERT INTO `product_size_bridge` VALUES (262680091, 7);
INSERT INTO `product_size_bridge` VALUES (7976948, 8);
INSERT INTO `product_size_bridge` VALUES (262680091, 8);
INSERT INTO `product_size_bridge` VALUES (7976948, 9);
INSERT INTO `product_size_bridge` VALUES (262680091, 9);
INSERT INTO `product_size_bridge` VALUES (7976948, 10);
INSERT INTO `product_size_bridge` VALUES (190321339, 11);
INSERT INTO `product_size_bridge` VALUES (190321339, 12);
INSERT INTO `product_size_bridge` VALUES (190321339, 13);
INSERT INTO `product_size_bridge` VALUES (190321339, 14);
INSERT INTO `product_size_bridge` VALUES (275262532, 15);
INSERT INTO `product_size_bridge` VALUES (275262532, 16);
INSERT INTO `product_size_bridge` VALUES (275262532, 17);
INSERT INTO `product_size_bridge` VALUES (275262532, 18);
INSERT INTO `product_size_bridge` VALUES (275262532, 19);

-- ----------------------------
-- Table structure for size_dim
-- ----------------------------
DROP TABLE IF EXISTS `size_dim`;
CREATE TABLE `size_dim`  (
  `size_id` int NOT NULL AUTO_INCREMENT,
  `size_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`size_id`) USING BTREE,
  UNIQUE INDEX `size_value`(`size_value` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of size_dim
-- ----------------------------
INSERT INTO `size_dim` VALUES (1, '');
INSERT INTO `size_dim` VALUES (14, '36-37');
INSERT INTO `size_dim` VALUES (10, '38');
INSERT INTO `size_dim` VALUES (13, '38-39');
INSERT INTO `size_dim` VALUES (9, '39');
INSERT INTO `size_dim` VALUES (19, '39 (Chân 24.5cm)');
INSERT INTO `size_dim` VALUES (8, '40');
INSERT INTO `size_dim` VALUES (18, '40 (Chân 25.5cm)');
INSERT INTO `size_dim` VALUES (7, '41');
INSERT INTO `size_dim` VALUES (17, '41 (Chân 26cm)');
INSERT INTO `size_dim` VALUES (6, '42');
INSERT INTO `size_dim` VALUES (16, '42 (Chân 27cm)');
INSERT INTO `size_dim` VALUES (12, '42-43');
INSERT INTO `size_dim` VALUES (5, '43');
INSERT INTO `size_dim` VALUES (15, '43 (Chân 27.5cm)');
INSERT INTO `size_dim` VALUES (4, '44');
INSERT INTO `size_dim` VALUES (11, '44-45');
INSERT INTO `size_dim` VALUES (3, '45');
INSERT INTO `size_dim` VALUES (2, '46');

SET FOREIGN_KEY_CHECKS = 1;
