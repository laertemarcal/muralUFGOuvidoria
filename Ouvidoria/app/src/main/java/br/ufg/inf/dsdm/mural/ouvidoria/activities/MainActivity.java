package br.ufg.inf.dsdm.mural.ouvidoria.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.ufg.inf.dsdm.mural.ouvidoria.R;
import br.ufg.inf.dsdm.mural.ouvidoria.model.Mensagem;
import br.ufg.inf.dsdm.mural.ouvidoria.services.OuvidoriaService;


public class MainActivity extends Activity {


    private EditText mEditTextNome;
    private EditText mEditTextEmail;
    private EditText mEditTextCpf;
    private EditText mEditTextTelefone;
    private EditText mEditTextMessage;

    private ImageView mImageViewThumbnail;

    private CheckBox mCheckBoxAnonimo;

    private Button mButtonAttachment;
    private Button mButtonSend;

    private Spinner mSpinnerAssunto;

    private String mSubject;
    private String mMessage;
    private String mName;
    private String mCpf;
    private String mTelefone;
    private String mEmail;

    private File mFileAttachment;

    private Mensagem mensagem;

    private static final int PHOTO_PICKER_ID = 1;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextNome = (EditText) findViewById(R.id.editTextNome);
        mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextCpf = (EditText) findViewById(R.id.editTextCpf);
        mEditTextTelefone = (EditText) findViewById(R.id.editTextPhone);
        mEditTextMessage = (EditText) findViewById(R.id.editTextMessage);

        mImageViewThumbnail = (ImageView) findViewById(R.id.imageView);

        mCheckBoxAnonimo = (CheckBox) findViewById(R.id.checkBox);
        mCheckBoxAnonimo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCheckBoxAnonimo.isChecked()) {
                    mEditTextNome.setText("");
                    mEditTextNome.setFocusable(false);
                    mEditTextNome.setFocusableInTouchMode(false);
                    mEditTextNome.setClickable(false);

                    mEditTextEmail.setText("");
                    mEditTextEmail.setFocusable(false);
                    mEditTextEmail.setFocusableInTouchMode(false);
                    mEditTextEmail.setClickable(false);

                    mEditTextCpf.setText("");
                    mEditTextCpf.setFocusable(false);
                    mEditTextCpf.setFocusableInTouchMode(false);
                    mEditTextCpf.setClickable(false);

                    mEditTextTelefone.setText("");
                    mEditTextTelefone.setFocusable(false);
                    mEditTextTelefone.setFocusableInTouchMode(false);
                    mEditTextTelefone.setClickable(false);
                } else {
                    mEditTextNome.setFocusable(true);
                    mEditTextNome.setFocusableInTouchMode(true);
                    mEditTextNome.setClickable(true);

                    mEditTextEmail.setFocusable(true);
                    mEditTextEmail.setFocusableInTouchMode(true);
                    mEditTextEmail.setClickable(true);

                    mEditTextCpf.setFocusable(true);
                    mEditTextCpf.setFocusableInTouchMode(true);
                    mEditTextCpf.setClickable(true);

                    mEditTextTelefone.setFocusable(true);
                    mEditTextTelefone.setFocusableInTouchMode(true);
                    mEditTextTelefone.setClickable(true);
                }
            }
        });

        mSpinnerAssunto = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> assuntoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.subject_options_array));
        mSpinnerAssunto.setAdapter(assuntoAdapter);

        mButtonAttachment = (Button) findViewById(R.id.buttonAttachment);
        mButtonAttachment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAttachmentOptions();
            }
        });


        mButtonSend = (Button) findViewById(R.id.buttonSend);
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mSubject = mSpinnerAssunto.getSelectedItem().toString();
                mMessage = mEditTextMessage.getText().toString();
                mName = mEditTextNome.getText().toString();
                mCpf = mEditTextCpf.getText().toString();
                mEmail = mEditTextEmail.getText().toString();
                mTelefone = mEditTextTelefone.getText().toString();

                if (!mCheckBoxAnonimo.isChecked() && (mName.equals("") || mName == null)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_field), Toast.LENGTH_LONG).show();
                    return;
                }

                if ((("").equals(mMessage) || mMessage == null)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_null_message), Toast.LENGTH_LONG).show();
                    return;
                }

                OuvidoriaService os = new OuvidoriaService(MainActivity.this);
                if (mCheckBoxAnonimo.isChecked()) {
                    mensagem = new Mensagem(mSubject, mMessage);
                } else {
                    mensagem = new Mensagem(mName, mEmail, mCpf, mTelefone, mSubject, mMessage);
                }
                if (mFileAttachment != null && mFileAttachment.length() != 0) {
                    mensagem.setAnexo(mFileAttachment);
                }
                os.execute(mensagem);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_PICKER_ID && resultCode == RESULT_OK) {
            mFileAttachment = new File(this.getCacheDir(), "anexo.png");
            try {
                mFileAttachment.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri imageUri = data.getData();
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] bitmapdata = stream.toByteArray();

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mFileAttachment);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

                long sizeInKb = mFileAttachment.length() / 1024;
                long sizeInMb = sizeInKb / 1024;

                if (sizeInMb > 1) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_size_limit), Toast.LENGTH_LONG).show();
                    mFileAttachment.delete();
                } else {
                    Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 300, 300, true);
                    mImageViewThumbnail.setImageBitmap(scaled);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAttachmentOptions() {
        Intent pickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooserIntent = Intent.createChooser(pickerIntent, getResources().getString(R.string.title_attachment_options_dialog));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        startActivityForResult(chooserIntent, PHOTO_PICKER_ID);
    }
}
