DELETE FROM account;
INSERT INTO `finaldb`.`account` (`id`, `address`, `avatar`, `created_date`, `email`, `name`, `password`, `phone`, `role`, `status`) VALUES (1, NULL, 'https://firebasestorage.googleapis.com/v0/b/phone-c4bc5.appspot.com/o/default_avatar.jpg?alt=media&token=0ff85744-9209-457b-aaf8-66d1f6893155', '2023-12-25 20:39:38.000000', 'admin@gmail.com', 'Administrator', 'admin', NULL, 'admin', 'active');
INSERT INTO `finaldb`.`account` (`id`, `address`, `avatar`, `created_date`, `email`, `name`, `password`, `phone`, `role`, `status`) VALUES (2, NULL, 'https://firebasestorage.googleapis.com/v0/b/phone-c4bc5.appspot.com/o/default_avatar.jpg?alt=media&token=0ff85744-9209-457b-aaf8-66d1f6893155', '2023-12-26 20:56:28.027701', 'user1@gmail.com', 'User 1', 'user1', NULL, 'user', 'active');
INSERT INTO `finaldb`.`account` (`id`, `address`, `avatar`, `created_date`, `email`, `name`, `password`, `phone`, `role`, `status`) VALUES (3, NULL, 'https://firebasestorage.googleapis.com/v0/b/phone-c4bc5.appspot.com/o/default_avatar.jpg?alt=media&token=0ff85744-9209-457b-aaf8-66d1f6893155', '2023-12-26 20:57:22.340114', 'user2@gmail.com', 'User 2', 'user2', NULL, 'user', 'locked');
INSERT INTO `finaldb`.`account` (`id`, `address`, `avatar`, `created_date`, `email`, `name`, `password`, `phone`, `role`, `status`) VALUES (4, NULL, 'https://firebasestorage.googleapis.com/v0/b/phone-c4bc5.appspot.com/o/default_avatar.jpg?alt=media&token=0ff85744-9209-457b-aaf8-66d1f6893155', '2023-12-26 20:58:04.768754', 'user3@gmail.com', 'User 3', 'user3', NULL, 'user', 'new');
INSERT INTO `finaldb`.`account` (`id`, `address`, `avatar`, `created_date`, `email`, `name`, `password`, `phone`, `role`, `status`) VALUES (5, 'Ho Chi Minh city', 'https://firebasestorage.googleapis.com/v0/b/phone-c4bc5.appspot.com/o/default_avatar.jpg?alt=media&token=0ff85744-9209-457b-aaf8-66d1f6893155', '2023-12-26 21:15:30.327623', NULL, 'Customer 1', NULL, '0123456789', 'customer', 'new');

DELETE FROM product;
INSERT INTO `finaldb`.`product` (`id`, `barcode`, `category`, `created_date`, `import_price`, `name`, `retail_price`) VALUES (1, '100001', 'Food', '2023-12-26 21:01:28.009289', 100000, 'Product 1', 120000);
INSERT INTO `finaldb`.`product` (`id`, `barcode`, `category`, `created_date`, `import_price`, `name`, `retail_price`) VALUES (2, '100002', 'Health', '2023-12-26 21:04:46.361518', 50000, 'Product 2', 60000);
INSERT INTO `finaldb`.`product` (`id`, `barcode`, `category`, `created_date`, `import_price`, `name`, `retail_price`) VALUES (3, '100003', 'Tool', '2023-12-26 21:06:11.409000', 650000, 'Product 3', 670000);

DELETE FROM orders;
INSERT INTO `finaldb`.`orders` (`id`, `created_date`, `customer_id`, `profit`, `total_price`) VALUES (1, '2023-12-26 21:15:34.155690', 5, 40000, 240000);
INSERT INTO `finaldb`.`orders` (`id`, `created_date`, `customer_id`, `profit`, `total_price`) VALUES (2, '2023-12-26 21:22:08.354929', 5, 30000, 730000);

DELETE FROM order_info;
INSERT INTO `finaldb`.`order_info` (`id`, `created_date`, `customer_id`, `order_id`, `product_id`, `quantity`, `total_price`) VALUES (1, '2023-12-26 21:15:34.180995', 5, 1, 1, 2, 120000);
INSERT INTO `finaldb`.`order_info` (`id`, `created_date`, `customer_id`, `order_id`, `product_id`, `quantity`, `total_price`) VALUES (2, '2023-12-26 21:22:08.365714', 5, 2, 2, 1, 60000);
INSERT INTO `finaldb`.`order_info` (`id`, `created_date`, `customer_id`, `order_id`, `product_id`, `quantity`, `total_price`) VALUES (3, '2023-12-26 21:22:08.496621', 5, 2, 3, 1, 670000);
