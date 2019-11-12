# Android Image Share Library

### Installation

# Step 1
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
# Step 2
```
  dependencies {
  
            implementation 'com.github.wefyns:android-image-share-library:1.0'
		
	}
```

# How to use

### Gradle
```
android {
        def contentProviderAuthority = applicationId + ".file_provider"
        // Creates a placeholder property to use in the manifest.
        manifestPlaceholders = [contentProviderAuthority: contentProviderAuthority]
        // Adds a new field for the authority to the BuildConfig class.
        buildConfigField("String", "CONTENT_PROVIDER_AUTHORITY","\"${contentProviderAuthority}\"")
        }
```
### res
- xml
 fileprovider.xml
```
<?xml version="1.0" encoding="utf-8"?>
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">
    <paths>
        <cache-path
            name="shared_image"
            path="images" />
    </paths>
</PreferenceScreen>
```

### Manifest
```
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${contentProviderAuthority}"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/fileprovider" />
        </provider>
```
### Activity
```
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        ShareCore.setup(this, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
    }
```
