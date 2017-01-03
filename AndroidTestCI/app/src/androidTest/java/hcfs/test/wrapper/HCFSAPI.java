package hcfs.test.wrapper;

import com.hopebaytech.hcfsmgmt.utils.HCFSApiUtils;
import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.json.JSONException;
import java.util.Map;
import hcfs.test.utils.JsonUtils;

public class HCFSAPI {

    private static final String THIS_CLASS = HCFSAPI.class.getSimpleName();

    public static class Result {
        public String originalJson;
        public boolean isSuccess;
        public int code;
        public Map<String, Object> data;

        @Override
        public String toString() {
            return "Json=" + originalJson + ", result=" + isSuccess + ", code=" + code + ", data=" + data.toString();
        }
    }

    public static Result checkPackageBoostStatus(String packageName) {
        String exeResult = HCFSApiUtils.checkPackageBoostStatus(packageName);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "checkPackageBoostStatus", "<" + packageName + "> Result <" + result + ">");
        return result;
    }


    public static Result checkRestoreStatus() {
        String exeResult = HCFSApiUtils.checkRestoreStatus();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "checkRestoreStatus", "Result <" + result + ">");
        return result;
    }

    public static Result stopUploadTeraData() {
        String exeResult = HCFSApiUtils.stopUploadTeraData();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "stopUploadTeraData", "Result <" + result + ">");
        return result;
    }

    public static Result collectSysLogs() {
        String exeResult = HCFSApiUtils.collectSysLogs();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "collectSysLogs", "Result <" + result + ">");
        return result;
    }

    public static Result getDirStatus(String path) {
        String exeResult = HCFSApiUtils.getDirStatus(path);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "getDirStatus", "<" + path + "> Result <" + result + ">");
        return result;
    }

    public static Result disableBooster() {
        String exeResult = HCFSApiUtils.disableBooster();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "disableBooster", "Result <" + result + ">");
        return result;
    }

    public static Result enableBooster(long size) {
        String exeResult = HCFSApiUtils.enableBooster(size);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "enableBooster", "<" + size + "> Result <" + result + ">");
        return result;
    }

    public static Result getFileStatus(String path) {
        String exeResult = HCFSApiUtils.getFileStatus(path);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "getFileStatus", "<" + path + "> Result <" + result + ">");
        return result;
    }

    public static Result getHCFSConfig(String key) {
        String exeResult = HCFSApiUtils.getHCFSConfig(key);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "getHCFSConfig", "<" + key + "> Result <" + result + ">");
        return result;
    }

    public static Result getOccupiedSize() {
        String exeResult = HCFSApiUtils.getOccupiedSize();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "getOccupiedSize", "Result <" + result + ">");
        return result;
    }

    public static Result getHCFSSyncStatus() {
        String exeResult = HCFSApiUtils.getHCFSSyncStatus();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "getHCFSSyncStatus", "Result <" + result + ">");
        return result;
    }

    public static Result notifyApplistChange() {
        String exeResult = HCFSApiUtils.notifyApplistChange();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "notifyApplistChange", "Result <" + result + ">");
        return result;
    }

    public static Result pin(String path, int pinPriority) {
        String exeResult = HCFSApiUtils.pin(path, pinPriority);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "Pin", "<" + path + "><" + pinPriority + "> Result <" + result + ">");
        return result;
    }

    public static Result getPinStatus(String path) {
        String exeResult = HCFSApiUtils.getPinStatus(path);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "getPinStatus", "<" + path + "> Result <" + result + ">");
        return result;
    }

    public static Result reloadConfig() {
        String exeResult = HCFSApiUtils.reloadConfig();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "reloadConfig", "Result <" + result + ">");
        return result;
    }

    public static Result resetXfer() {
        String exeResult = HCFSApiUtils.resetXfer();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "resetXfer", "Result <" + result + ">");
        return result;
    }

    public static Result setHCFSConfig(String key, String value) {
        String exeResult = HCFSApiUtils.setHCFSConfig(key, value);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "setHCFSConfig", "<" + key + "><" + value + "> Result <" + result + ">");
        return result;
    }

    public static Result setNotifyServer(String socketAddr) {
        String exeResult = HCFSApiUtils.setNotifyServer(socketAddr);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "setNotifyServer", "<" + socketAddr + "> Result <" + result + ">");
        return result;
    }

    public static Result setSwiftToken(String url, String token) {
        String exeResult = HCFSApiUtils.setSwiftToken(url, token);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "setSwiftToken", "<" + url + "><" + token + "> Result <" + result + ">");
        return result;
    }

    public static Result startUploadTeraData() {
        String exeResult = HCFSApiUtils.startUploadTeraData();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "startUploadTeraData", "Result <" + result + ">");
        return result;
    }

    public static Result getHCFSStat() {
        String exeResult = HCFSApiUtils.getHCFSStat();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "getHCFSStat", "Result <" + result + ">");
        return result;
    }

    public static Result setHCFSSyncStatus(int enabled) {
        String exeResult = HCFSApiUtils.setHCFSSyncStatus(enabled);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "setHCFSSyncStatus", "<" + enabled + "> Result <" + result + ">");
        return result;
    }

    public static Result triggerBoost() {
        String exeResult = HCFSApiUtils.triggerBoost();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "triggerBoost", "Result <" + result + ">");
        return result;
    }

    public static Result triggerRestore() {
        String exeResult = HCFSApiUtils.triggerRestore();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "triggerBoost", "Result <" + result + ">");
        return result;
    }

    public static Result triggerUnboost() {
        String exeResult = HCFSApiUtils.triggerUnboost();
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "triggerUnboost", "Result <" + result + ">");
        return result;
    }

    public static Result unpin(String path) {
        String exeResult = HCFSApiUtils.unpin(path);
        Result result = parse(exeResult);
        Logs.d(THIS_CLASS, "unpin", "<" + path + "> Result <" + result + ">");
        return result;
    }

    public static Result getEncryptedIMEI(String imei) {
        throw new RuntimeException("Unimplemented wrapper");
//        byte[] exeResult = HCFSApiUtils.getEncryptedIMEI(imei);
//        Result result = parse(exeResult);
//        Logs.d(THIS_CLASS, "getEncryptedIMEI", "<" + imei + "> Result <" + result + ">");
//        return result;
    }

    public static Result getDecryptedJsonString(String jsonString) {
        throw new RuntimeException("Unimplemented wrapper");
//        byte[] exeResult = HCFSApiUtils.getDecryptedJsonString(jsonString);
//        Result result = parse(exeResult);
//        Logs.d(THIS_CLASS, "getDecryptedJsonString", "<" + jsonString + "> Result <" + result + ">");
//        return result;
    }

    private static Result parse(String jsonResult) {
        try {
            Result result = new Result();
            result.originalJson = jsonResult;
            result.isSuccess = JsonUtils.getResult(jsonResult);
            result.code = JsonUtils.getCode(jsonResult);
            result.data = JsonUtils.toMap(JsonUtils.getDataObject(jsonResult));
            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
