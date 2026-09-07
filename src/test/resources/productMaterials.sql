INSERT INTO product_materials(product_id, material_id)
VALUES ((SELECT MAX(id) from products), 99990);