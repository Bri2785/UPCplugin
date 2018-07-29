SELECT
	(CASE WHEN (uom.id = displayWeightUom.id)
		 THEN 1
         ELSE COALESCE(uomconversion.multiply, -1)
         END) AS hasConversion,
    uom.name AS fromUom,
    displayWeightUom.name AS toUom,
    product.num,
    product.weight
FROM so
   INNER JOIN soItem ON soItem.soId = so.id
   INNER JOIN product ON product.id = soItem.productId
   INNER JOIN part ON part.id = product.partId
   LEFT JOIN uom ON product.weightUomId = uom.id
   LEFT JOIN uom AS displayWeightUom ON displayWeightUom.id = %2$s
   LEFT JOIN uomConversion ON uomConversion.fromUomId = uom.id AND uomConversion.toUomId = displayWeightUom.id
WHERE so.id = %1$s
  AND part.typeId = 10