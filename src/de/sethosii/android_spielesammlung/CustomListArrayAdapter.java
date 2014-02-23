package de.sethosii.android_spielesammlung;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomListArrayAdapter extends ArrayAdapter<String> {
	private static final String Tag = "CustomListArrayAdapter";
	private final Activity context;
	private final String[] names;

	static class ViewHolder {
		public TextView text;
		public TextView text_bottom;
		public ImageView image;
	}

	public CustomListArrayAdapter(Activity context, String[] names) {
		super(context, R.layout.listviewitem_main, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		Log.w(Tag, "startrow");
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listviewitem_main, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.top_Line);
			//Log.e(Tag, viewHolder.text.getText().toString());
			viewHolder.text_bottom = (TextView) rowView
					.findViewById(R.id.bottom_Line);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.game_icon);
			viewHolder.text.setText("huch");
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		String s = names[position];
		holder.text.setText(s);
		//Log.e(Tag, holder.text.getText().toString());
		if (s.startsWith("Mines")) {
			holder.text.setText("Mines");
			holder.text_bottom.setText("Mines");
			holder.image.setImageResource(R.drawable.minesweeper_icon);
		} else {
			if (s.startsWith("Sudoku")) {
				holder.text.setText("Sudoku");
				holder.text_bottom.setText("Sudoku");
				holder.image.setImageResource(R.drawable.sudoku_icon);
			} else {
				holder.text.setText("Spaceslider");
				holder.text_bottom.setText("Spaceslider");
				holder.image.setImageResource(R.drawable.spaceslider_icon);
			}
		}
		
		// holder.image.setImageResource(R.drawable.no);
		// } else {
		// holder.image.setImageResource(R.drawable.ok);
		// }
		// }
		return rowView;
	}
}
