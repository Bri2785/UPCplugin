SELECT bi_upctouom.id, productID, upcCode, uomFromId, uom.name AS UomName, uomconversion.multiply AS UomQty
FROM bi_upctouom
JOIN uomconversion ON uomconversion.`fromUomId` = bi_upctouom.`uomFromId` AND uomconversion.`toUomId`= 1
JOIN uom ON uom.`id` = uomconversion.`fromUomId`

WHERE productid = %1$s