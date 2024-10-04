var exec = require('cordova/exec');

exports.checkVersion = function(success, error) {
    exec(success, error, 'VersionChecker', 'checkVersion', []);
};