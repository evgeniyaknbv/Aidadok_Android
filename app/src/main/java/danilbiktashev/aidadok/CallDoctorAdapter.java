package danilbiktashev.aidadok;

import android.graphics.Typeface;
import android.util.Log;
import android.widget.BaseAdapter;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mikhailkoroteev on 23.05.17.
 */

public class CallDoctorAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<String> objects;

    CallDoctorAdapter(Context context, ArrayList<String> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.call_doctor_item, parent, false);
        }

        String p = getSymp(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка

        ((TextView) view.findViewById(R.id.title)).setText(p);

        ((TextView) view.findViewById(R.id.title)).setTypeface(Typeface.createFromAsset(ctx.getAssets(),"ProximaNovaRegular.otf"));

        return view;
    }

    // товар по позиции
    String getSymp(int position) {
        return ((String) getItem(position));
    }

    // содержимое корзины
    ArrayList<String> getBox() {
        ArrayList<String> box = new ArrayList<>();;
        return box;
    }

}