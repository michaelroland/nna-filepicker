/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.nononsenseapps.filepicker.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.sample.dropbox.DropboxFilePickerActivity;
import com.nononsenseapps.filepicker.sample.dropbox.DropboxFilePickerActivity2;
import com.nononsenseapps.filepicker.sample.dropbox.DropboxSyncHelper;
import com.nononsenseapps.filepicker.sample.ftp.FtpPickerActivity;
import com.nononsenseapps.filepicker.sample.ftp.FtpPickerActivity2;

import java.util.ArrayList;


public class NoNonsenseFilePicker extends Activity {

    private static final int CODE_SD = 0;
    private static final int CODE_DB = 1;
    private static final int CODE_FTP = 2;
    private TextView textView;
    private DropboxAPI<AndroidAuthSession> mDBApi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_nonsense_file_picker);

        final CheckBox checkAllowCreateDir =
                (CheckBox) findViewById(R.id.checkAllowCreateDir);
        final CheckBox checkAllowMultiple =
                (CheckBox) findViewById(R.id.checkAllowMultiple);
        final CheckBox checkAllowExistingFile =
                (CheckBox) findViewById(R.id.checkAllowExistingFile);
        final CheckBox checkLightTheme =
                (CheckBox) findViewById(R.id.checkLightTheme);
        final RadioGroup radioGroup =
                (RadioGroup) findViewById(R.id.radioGroup);
        textView = (TextView) findViewById(R.id.text);

        findViewById(R.id.button_sd)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Intent i;

                        if (checkLightTheme.isChecked()) {
                            i = new Intent(NoNonsenseFilePicker.this,
                                    FilePickerActivity2.class);
                        } else {
                            i = new Intent(NoNonsenseFilePicker.this,
                                    FilePickerActivity.class);
                        }
                        i.setAction(Intent.ACTION_GET_CONTENT);

                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE,
                                checkAllowMultiple.isChecked());
                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR,
                                checkAllowCreateDir.isChecked());
                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_EXISTING_FILE,
                                checkAllowExistingFile.isChecked());

                        // What mode is selected
                        final int mode;
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.radioDir:
                                mode = FilePickerActivity.MODE_DIR;
                                break;
                            case R.id.radioFilesAndDirs:
                                mode = FilePickerActivity.MODE_FILE_AND_DIR;
                                break;
                            case R.id.radioNewFile:
                                mode = FilePickerActivity.MODE_NEW_FILE;
                                break;
                            case R.id.radioFile:
                            default:
                                mode = FilePickerActivity.MODE_FILE;
                                break;
                        }

                        i.putExtra(FilePickerActivity.EXTRA_MODE, mode);

                        // Warn about invalid combination
                        if (mode == FilePickerActivity.MODE_NEW_FILE &&
                                checkAllowMultiple.isChecked()) {
                            Toast.makeText(NoNonsenseFilePicker.this,
                                    "'New file' does not support multiple items",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        startActivityForResult(i, CODE_SD);
                    }
                });

        findViewById(R.id.button_image)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Intent i;

                        if (checkLightTheme.isChecked()) {
                            i = new Intent(NoNonsenseFilePicker.this,
                                    MultimediaPickerActivity2.class);
                        } else {
                            i = new Intent(NoNonsenseFilePicker.this,
                                    MultimediaPickerActivity.class);
                        }
                        i.setAction(Intent.ACTION_GET_CONTENT);

                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE,
                                checkAllowMultiple.isChecked());
                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR,
                                checkAllowCreateDir.isChecked());
                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_EXISTING_FILE,
                                checkAllowExistingFile.isChecked());

                        // What mode is selected (makes no sense to restrict to folders here)
                        final int mode;
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.radioFilesAndDirs:
                                mode =
                                        AbstractFilePickerFragment.MODE_FILE_AND_DIR;
                                break;
                            case R.id.radioNewFile:
                                mode = FilePickerActivity.MODE_NEW_FILE;
                                break;
                            case R.id.radioFile:
                            default:
                                mode = AbstractFilePickerFragment.MODE_FILE;
                                break;
                        }

                        i.putExtra(FilePickerActivity.EXTRA_MODE, mode);


                        startActivityForResult(i, CODE_SD);
                    }
                });

        findViewById(R.id.button_ftp)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Intent i;

                        if (checkLightTheme.isChecked()) {
                            i = new Intent(NoNonsenseFilePicker.this,
                                    FtpPickerActivity2.class);
                        } else {
                            i = new Intent(NoNonsenseFilePicker.this,
                                    FtpPickerActivity.class);
                        }
                        i.setAction(Intent.ACTION_GET_CONTENT);

                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE,
                                checkAllowMultiple.isChecked());
                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR,
                                checkAllowCreateDir.isChecked());
                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_EXISTING_FILE,
                                checkAllowExistingFile.isChecked());

                        // What mode is selected (makes no sense to restrict to folders here)
                        final int mode;
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.radioDir:
                                mode = AbstractFilePickerFragment.MODE_DIR;
                                break;
                            case R.id.radioFilesAndDirs:
                                mode =
                                        AbstractFilePickerFragment.MODE_FILE_AND_DIR;
                                break;
                            case R.id.radioNewFile:
                                mode = FilePickerActivity.MODE_NEW_FILE;
                                break;
                            case R.id.radioFile:
                            default:
                                mode = AbstractFilePickerFragment.MODE_FILE;
                                break;
                        }

                        i.putExtra(FilePickerActivity.EXTRA_MODE, mode);


                        startActivityForResult(i, CODE_FTP);
                    }
                });

        findViewById(R.id.button_dropbox)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        // First we must authorize the user
                        if (mDBApi == null) {
                            mDBApi = DropboxSyncHelper
                                    .getDBApi(NoNonsenseFilePicker.this);
                        }

                        // If not authorized, then ask user for login/permission
                        if (!mDBApi.getSession().isLinked()) {
                            mDBApi.getSession().startOAuth2Authentication(
                                    NoNonsenseFilePicker.this);
                        } else {  // User is authorized, open file picker
                            Intent i;
                            if (checkLightTheme.isChecked()) {
                                i = new Intent(NoNonsenseFilePicker.this,
                                        DropboxFilePickerActivity2.class);
                            } else {
                                i = new Intent(NoNonsenseFilePicker.this,
                                        DropboxFilePickerActivity.class);
                            }

                            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE,
                                    checkAllowMultiple.isChecked());
                            i.putExtra(
                                    FilePickerActivity.EXTRA_ALLOW_CREATE_DIR,
                                    checkAllowCreateDir.isChecked());
                            i.putExtra(FilePickerActivity.EXTRA_ALLOW_EXISTING_FILE,
                                    checkAllowExistingFile.isChecked());

                            // What mode is selected
                            final int mode;
                            switch (radioGroup.getCheckedRadioButtonId()) {
                                case R.id.radioDir:
                                    mode = AbstractFilePickerFragment.MODE_DIR;
                                    break;
                                case R.id.radioFilesAndDirs:
                                    mode =
                                            AbstractFilePickerFragment.MODE_FILE_AND_DIR;
                                    break;
                                case R.id.radioNewFile:
                                    mode = FilePickerActivity.MODE_NEW_FILE;
                                    break;
                                case R.id.radioFile:
                                default:
                                    mode = AbstractFilePickerFragment.MODE_FILE;
                                    break;
                            }

                            i.putExtra(FilePickerActivity.EXTRA_MODE, mode);

                            startActivityForResult(i, CODE_DB);
                        }
                    }
                });
    }

    /**
     * This is entirely for Dropbox's benefit
     */
    protected void onResume() {
        super.onResume();

        if (mDBApi != null && mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                DropboxSyncHelper.saveToken(this, accessToken);
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.no_nonsense_file_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if ((CODE_SD == requestCode || CODE_DB == requestCode || CODE_FTP == requestCode) &&
                resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE,
                    false)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();
                    StringBuilder sb = new StringBuilder();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            sb.append(clip.getItemAt(i).getUri().toString());
                            sb.append("\n");
                        }
                    }

                    textView.setText(sb.toString());
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra(
                            FilePickerActivity.EXTRA_PATHS);
                    StringBuilder sb = new StringBuilder();

                    if (paths != null) {
                        for (String path : paths) {
                            sb.append(path);
                            sb.append("\n");
                        }
                    }
                    textView.setText(sb.toString());
                }
            } else {
                textView.setText(data.getData().toString());
            }
        }
    }

}
