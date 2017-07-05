package danilbiktashev.aidadok;

import android.graphics.Typeface;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mikhailkoroteev on 23.05.17.
 */

public class SympAdapter extends BaseAdapter {

        Context ctx;
        LayoutInflater lInflater;
        ArrayList<symp> objects;

        SympAdapter(Context context, ArrayList<symp> products) {
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
                view = lInflater.inflate(R.layout.sympitem, parent, false);
            }

            symp p = getSymp(position);

            // заполняем View в пункте списка данными из товаров: наименование, цена
            // и картинка
            ((TextView) view.findViewById(R.id.descr)).setText(p.descr);
            ((TextView) view.findViewById(R.id.title)).setText(p.name);

            ((TextView) view.findViewById(R.id.title)).setTypeface(Typeface.createFromAsset(ctx.getAssets(),"systopie_bold.otf"));
            ((TextView) view.findViewById(R.id.descr)).setTypeface(Typeface.createFromAsset(ctx.getAssets(),"ProximaNovaRegular.otf"));

            return view;
        }

        // товар по позиции
        symp getSymp(int position) {
            return ((symp) getItem(position));
        }

        // содержимое корзины
        ArrayList<symp> getBox() {
            ArrayList<symp> box = new ArrayList<symp>();
            return box;
        }

}
