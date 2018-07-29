SELECT UOM.id, uom.`name` as code, uomconversion.`multiply`
FROM uomconversion
JOIN uom ON uom.`id` = uomconversion.`fromUomId`
WHERE uomconversion.`toUomId` = 1