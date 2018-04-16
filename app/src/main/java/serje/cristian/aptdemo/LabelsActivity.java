package serje.cristian.aptdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cristian.serje
 * @since 2017.07.14
 */
public class LabelsActivity extends AppCompatActivity {
    @BindView(R.id.result_textview)
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labels);

        ButterKnife.bind(this);

        final Bundle extras = getIntent().getExtras();
        final Set<String> keySet = extras.keySet();
        StringBuilder sb = new StringBuilder();
        for (final String key : keySet) {
            sb.append('\"');
            sb.append(key);
            sb.append("\":\"");
            sb.append(extras.get(key));
            sb.append("\", \n\n");
        }

        resultTextView.setText(sb.toString());
    }
}
