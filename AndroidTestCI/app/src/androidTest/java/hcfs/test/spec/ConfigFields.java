package hcfs.test.spec;

public class ConfigFields {
	
	public static final String METAPATH = "metapath";
	public static final String BLOCKPATH = "blockpath";
	public static final String CACHE_SOFT_LIMIT = "cache_soft_limit";
	public static final String CACHE_HARD_LIMIT = "cache_hard_limit";
	public static final String MAX_BLOCK_SIZE = "max_block_size";
	public static final String CURRENT_BACKEND = "current_backend";
	
	public static final String SWIFT_ACCOUNT = "swift_account";
	public static final String SWIFT_USER = "swift_user";
	public static final String SWIFT_PASS = "swift_pass";
	public static final String SWIFT_URL = "swift_url";
	public static final String SWIFT_CONTAINER = "swift_container";
	public static final String SWIFT_PROTOCOL = "swift_protocol";
	
	public static final String S3_ACCESS = "s3_access";
	public static final String S3_SECRET = "s3_secret";
	public static final String S3_URL = "s3_url";
	public static final String S3_BUCKET = "s3_bucket";
	public static final String S3_PROTOCOL = "s3_protocol";
	
	public static final String LOG_LEVEL = "log_level";
	public static final String LOG_PATH = "log_path";

	public static final String[] HCFS_CONFIG_FILE_FIELDS = { METAPATH, BLOCKPATH, CACHE_SOFT_LIMIT, CACHE_HARD_LIMIT,
			MAX_BLOCK_SIZE, CURRENT_BACKEND, SWIFT_ACCOUNT, SWIFT_USER, SWIFT_PASS, SWIFT_URL,
			SWIFT_CONTAINER, SWIFT_PROTOCOL, S3_ACCESS, S3_SECRET, S3_URL, S3_BUCKET,
			S3_PROTOCOL, LOG_LEVEL, LOG_PATH};
}
