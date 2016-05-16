# NoNonsense-FilePicker

See [NoNonsense-FilePicker](https://github.com/spacecowboy/NoNonsense-FilePicker).



## How to use the file picker


### Include the file picker activity

Add the activity to your the AndroidManifext.xml of your app. Note that the
theme set in the manifest is important.

```xml
    <activity
       android:name="com.nononsenseapps.filepicker.FilePickerActivity"
       android:theme="@style/FilePickerTheme" />
```

### Configure the theme

You must **set the theme** on the activity, but you can configure it to
match your existing application theme.

```xml
    <!-- FilePicker theme. -->
    <!-- You can also inherit from NNF_BaseTheme -->
    <style name="FilePickerTheme" parent="NNF_BaseTheme.Light">
        <item name="alertDialogTheme">@style/Theme.AppCompat.Light.Dialog.Alert</item>
        <item name="nnf_toolbarTheme">@style/AppTheme</item>
        <item name="nnf_toolbarTextAppearence">@style/TextAppearance.AppCompat.Widget.ActionBar.Title.Inverse</item>
    </style>
```

### Starting the picker in your app

```java
    Intent i = new Intent(getActivity(), FilePickerActivity.class);

    // EXTRA_ALLOW_MULTIPLE (boolean):
    //     true:  Multiple files may be selected at once
    //            (incompatible with MODE_NEW_FILE)
    //     false: Only one file may be selected
    i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);

    // EXTRA_ALLOW_CREATE_DIR (boolean):
    //     true:  Allow creation of new directories
    //     false: Only existing directories can be used
    i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);

    // EXTRA_ALLOW_EXISTING_FILE (boolean):
    //     (only used in combination with MODE_NEW_FILE)
    //     true:  Allow selection of existing files
    //     false: Only new, non-existant file names may be used
    i.putExtra(FilePickerActivity.EXTRA_ALLOW_EXISTING_FILE, true);

    // EXTRA_MODE (int):
    //     (multiple modes may be combined with logical OR)
    //     MODE_FILE: Files may be selected
    //     MODE_DIR: Directories may be selected
    //     MODE_FILE_AND_DIR: Convenience value for MODE_FILE | MODE_DIR
    //     MODE_NEW_FILE: Allow creation of new files
    //     MODE_READABLE: Only show readable files
    //     MODE_WRITABLE: Only show writable files
    //     MODE_EXECUTABLE: Only show executable files
    //     MODE_HIDDEN: Only show hidden files
    i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE |
                                              FilePickerActivity.MODE_NEW_FILE |
                                              FilePickerActivity.MODE_WRITABLE);
    // EXTRA_BASE_PATHS (String[]):
    //     List of paths that should always be listed (even if inaccessible
    //     or invisible due to EXTRA_MODE restrictions)
    i.putExtra(FilePickerActivity.EXTRA_BASE_PATHS, new String[]{
            getActivity().getFilesDir().getAbsolutePath(),
            Environment.getExternalStorageDirectory().getAbsolutePath(),
    });

    // EXTRA_START_PATH (String):
    //     Directory that should be shown as current directory in the file
    //     picker. This may be a complete file name and the file name is shown
    //     in the new file text field for MODE_NEW_FILE.
    i.putExtra(FilePickerActivity.EXTRA_START_PATH,
               Environment.getExternalStorageDirectory().getAbsolutePath());

    startActivityForResult(i, RESULT_FILEPICKER);
```

### Handling the result

If you have a minimum requirement of Jelly Bean (API 16) and above,
you can skip the second method.

```java
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_FILEPICKER:
                if (resultCode == FilePickerActivity.RESULT_OK) {
                    if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                        // For JellyBean and above
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ClipData clip = data.getClipData();

                            if (clip != null) {
                                for (int i = 0; i < clip.getItemCount(); i++) {
                                    Uri uri = clip.getItemAt(i).getUri();
                                    // Do something with the URI
                                }
                            }
                        // For Ice Cream Sandwich
                        } else {
                            ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);

                            if (paths != null) {
                                for (String path: paths) {
                                    Uri uri = Uri.parse(path);
                                    // Do something with the URI
                                }
                            }
                        }
                    } else {
                        Uri uri = data.getData();
                        // Do something with the URI
                    }
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
```

### Include permission in your manifest

Don't forget to include the permission for access to the external storage, if needed.

```xml
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

