package br.ufg.inf.dsdm.mural.ouvidoria.services;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;

import br.ufg.inf.dsdm.mural.ouvidoria.R;
import br.ufg.inf.dsdm.mural.ouvidoria.model.Mensagem;

/**
 * Created by Laerte on 03/07/2015.
 */
public class OuvidoriaService extends AsyncTask<Mensagem, Void, Void> {
    private String url = "http://private-b99f3-muralouvidoria.apiary-mock.com/ouvidoria/messages";
    private int httpStatus;
    private String response;
    private ProgressDialog dialog;
    private Context handler;

    public OuvidoriaService(Context handler) {
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Mensagem... mensagem) {

        Mensagem message = mensagem[0];

        try {
            HttpRequest request = HttpRequest.post(url);
            request.part("name", message.getNome());
            request.part("mail", message.getEmail());
            request.part("cpf", message.getCpf());
            request.part("phone", message.getTelefone());
            request.part("subject", message.getAssunto());
            request.part("message", message.getMensagem());
            if (message.getAnexo() != null && message.getAnexo().length() != 0) {
                request.part("attachment", "anexo.png", message.getAnexo());
                Log.i("anexo", message.getAnexo().getName());
                Log.i("tamanho anexo", message.getAnexo().length() + "");
            }
            httpStatus = request.code();
            response = request.body();
            Log.i("URL", url);
            Log.i("Response", response);
            Log.i("Nome", message.getNome());
            Log.i("E-mail", message.getEmail());
            Log.i("CPF", message.getCpf());
            Log.i("Telefone", message.getTelefone());
            Log.i("Assunto", message.getAssunto());
            Log.i("Mensagem", message.getMensagem());
        } catch (Exception e) {
            Log.e("REQUEST ERROR", e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(handler);
        dialog.setMessage(handler.getResources().getString(R.string.text_request));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
        if (httpStatus == 200) {
            showNotification();
        }
    }

    private void showNotification() {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(handler)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(handler.getResources().getString(R.string.app_name))
                        .setContentText(handler.getResources().getString(R.string.message_sent));
        NotificationManager mNotificationManager =
                (NotificationManager) handler.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
        Toast.makeText(handler, R.string.message_sent, Toast.LENGTH_SHORT).show();
    }
}
