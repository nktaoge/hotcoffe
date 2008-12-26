DROP TABLE IF EXISTS `hotcoffe`.`hot_ponto`;
CREATE TABLE  `hotcoffe`.`hot_ponto` (
  `ponto_id` int(10) unsigned NOT NULL auto_increment,
  `nome` varchar(300) default NULL,
  `descricao` varchar(300) default NULL,
  `classe` varchar(200) default NULL,
  `data_hora` datetime NOT NULL,
  PRIMARY KEY  (`ponto_id`),
  UNIQUE KEY `Unico` USING HASH (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `hotcoffe`.`hot_ligacao`;
CREATE TABLE  `hotcoffe`.`hot_ligacao` (
  `ligacao_id` int(10) unsigned NOT NULL auto_increment,
  `ponto_id_a` int(10) unsigned NOT NULL,
  `ponto_id_b` int(10) unsigned NOT NULL,
  `data_hora` datetime NOT NULL,
  PRIMARY KEY  (`ligacao_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `hotcoffe`.`hot_svp`;
CREATE TABLE  `hotcoffe`.`hot_svp` (
  `id_svp` int(10) unsigned NOT NULL auto_increment,
  `ponto_id_s` int(10) unsigned NOT NULL,
  `ponto_id_v` int(10) unsigned NOT NULL,
  `ponto_id_p` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id_svp`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
