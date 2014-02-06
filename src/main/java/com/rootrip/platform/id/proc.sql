CREATE PROCEDURE xproxy_id_server_next(keyname varchar(64)) 
BEGIN
 DECLARE cur BIGINT;
 DECLARE uped BIGINT;
	
 SET uped=0;
 WHILE uped<=0 DO
  SELECT curid INTO cur FROM xproxy_id_server WHERE idxname=keyname;
  IF cur is null THEN
  	INSERT INTO xproxy_id_server(idxname,curid) VALUES(keyname,"1") ON DUPLICATE KEY UPDATE curid=curid;
  END IF;
  UPDATE xproxy_id_server SET curid=cur+500 WHERE idxname=keyname AND curid=cur;
	SET uped=row_count();
 END WHILE;
 SELECT cur;
END 