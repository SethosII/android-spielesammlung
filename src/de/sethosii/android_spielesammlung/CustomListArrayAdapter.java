package de.sethosii.android_spielesammlung;

import java.util.ArrayList;

import de.sethosii.android_spielesammlung.persistence.MinesPersistentGameData;
import de.sethosii.android_spielesammlung.persistence.PersistenceHandler;
import de.sethosii.android_spielesammlung.persistence.SudokuPersistentGameData;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomListArrayAdapter extends ArrayAdapter<String> {//BaseAdapter{
	private static final String Tag = "CustomListArrayAdapter";
	private Activity activity;
	private static LayoutInflater inflater=null;
	private ArrayList<String> names;

	static class ViewHolder {
		public TextView tt;
		public TextView bt;
		public ImageView image;
	}

	public CustomListArrayAdapter(Activity a, ArrayList<String> values) {
		super(a, R.layout.listviewitem_main, values);
		this.activity = a;
		this.names = values;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		ViewHolder holder;
		
        if(convertView==null){
            Log.w(Tag, "ist null");
            vi = inflater.inflate(R.layout.listviewitem_main, null);
            holder = new ViewHolder();
            holder.tt = (TextView) vi.findViewById(R.id.top_Line);
            holder.bt=(TextView)vi.findViewById(R.id.bottom_Line);
            holder.image=(ImageView)vi.findViewById(R.id.game_icon);
            
           /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else  
            holder=(ViewHolder)vi.getTag();

        Log.w(Tag, "startrow");
        
        if(names.size()<=0)
        {
            holder.tt.setText("No Data");
            
        }
        else
        {
        String s = names.get(position);
        Log.w(Tag, s + position);
        if (s.startsWith("Mines")) {
        	Log.w(Tag, "mine");
        	holder.tt.setText("Mines");
        	// load mines highscore
			MinesPersistentGameData mpgd = PersistenceHandler.getMinesPersistentGameData(activity);
			if(mpgd!=null)
			{
				// time < 10 s add 0 at beginning
				String seconds;
				long tmp = (mpgd.scoring[0].score/1000)%60;
				if (tmp < 10) {
					seconds = "0"+Long.toString(tmp);
				} else {
					seconds = Long.toString(tmp);
				}
				String minutes = Long.toString((mpgd.scoring[0].score/(1000*60))%60);
				holder.bt.setText("Highscore: " + minutes + ":"+ seconds);
			}
			else{
				holder.bt.setText("Highscore: ");
			}

        	holder.image.setImageResource(R.drawable.minesweeper_icon);
		} else {
			if (s.startsWith("Sudoku")) {
				Log.w(Tag, "sudo");
				holder.tt.setText("Sudoku");
				//loads sudoku highscore
				SudokuPersistentGameData spgd = PersistenceHandler.getSudokuPersistentGameData(activity);
				if(spgd!=null)
				{
					// time < 10 s add 0 at beginning
					String seconds;
					long tmp = (spgd.scoring[0].score/1000)%60;
					if (tmp < 10) {
						seconds = "0"+Long.toString(tmp);
					} else {
						seconds = Long.toString(tmp);
					}
					String minutes = Long.toString((spgd.scoring[0].score/(1000*60))%60);
					holder.bt.setText("Highscores: " + minutes + ":"+ seconds);
				}
				else{
					holder.bt.setText("Highscores: ");
				}
				holder.image.setImageResource(R.drawable.sudoku_icon);
			} else {
				Log.w(Tag, "space");
				holder.tt.setText("Spaceslider");
				holder.bt.setText("Highscores: 1432");
				holder.image.setImageResource(R.drawable.spaceslider_icon);
			}
		}
        }

        return vi;
		
		
		
		
//		View rowView = convertView;
//		Log.w(Tag, "startrow");
//		if (rowView == null) {
//			LayoutInflater inflater = context.getLayoutInflater();
//			rowView = inflater.inflate(R.layout.listviewitem_main, null);
//			ViewHolder viewHolder = new ViewHolder();
//			viewHolder.text = (TextView) rowView.findViewById(R.id.top_Line);
//			//Log.e(Tag, viewHolder.text.getText().toString());
//			viewHolder.text_bottom = (TextView) rowView
//					.findViewById(R.id.bottom_Line);
//			viewHolder.image = (ImageView) rowView.findViewById(R.id.game_icon);
//			viewHolder.text.setText("huch");
//			rowView.setTag(viewHolder);
//		}
//		
//		ViewHolder holder = (ViewHolder) rowView.getTag();
//		String s = names[position];
//		holder.text.setText(s);
//		//Log.e(Tag, holder.text.getText().toString());
//		if (s.startsWith("Mines")) {
//			holder.text.setText("Mines");
//			holder.text_bottom.setText("Mines");
//			holder.image.setImageResource(R.drawable.minesweeper_icon);
//		} else {
//			if (s.startsWith("Sudoku")) {
//				holder.text.setText("Sudoku");
//				holder.text_bottom.setText("Sudoku");
//				holder.image.setImageResource(R.drawable.sudoku_icon);
//			} else {
//				holder.text.setText("Spaceslider");
//				holder.text_bottom.setText("Spaceslider");
//				holder.image.setImageResource(R.drawable.spaceslider_icon);
//			}
//		}
		
		// holder.image.setImageResource(R.drawable.no);
		// } else {
		// holder.image.setImageResource(R.drawable.ok);
		// }
		// }
		//return rowView;
	}
}
