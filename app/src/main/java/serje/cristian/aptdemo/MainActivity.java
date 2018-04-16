package serje.cristian.aptdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.annotations.BindValue;
import com.annotations.BundleBind;
import com.annotations.InProgress;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cristian.serje
 * @since 2017.07.14
 */
@InProgress
@BundleBind
public class MainActivity extends AppCompatActivity {
    @BindValue
    @BindView(R.id.address_edit)
    EditText addressTextView;
    @BindValue
    @BindView(R.id.name_edit)
    EditText nameTextView;
    @BindValue
    @BindView(R.id.lastname_edit)
    EditText lastNameTextView;
    @BindValue
    @BindView(R.id.email_edit)
    EditText emailTextView;
    @BindView(R.id.show_labels_btn)
    Button showLabelsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        showLabelsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityBundleBinder.openActivity(MainActivity.this, LabelsActivity.class);
            }
        });
    }
}
