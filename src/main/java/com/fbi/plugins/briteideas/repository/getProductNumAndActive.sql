SELECT p.activeFlag, p.num, p.taxableFlag, p.kitFlag, p.uomId, tr.code
FROM product p
LEFT JOIN taxRate tr ON tr.id = p.taxId
WHERE p.id = %1$s