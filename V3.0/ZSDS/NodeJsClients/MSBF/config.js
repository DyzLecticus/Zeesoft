var ZSDS_URL = process.env.ZSDS_URL || 'http://localhost:8080/ZSDS';
var ZSDS_DRH_PATH = process.env.ZSDS_DRH_PATH || '/dialogRequestHandler.json';

var config = {
	ZSDS_URL: ZSDS_URL,
	ZSDS_DRH_URL: ZSDS_URL + ZSDS_DRH_PATH
};

exports.config = config;
