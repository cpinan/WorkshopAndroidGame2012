package com.carlospinan.workshopandroid.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Canvas;

public class LayoutManagerHelper {

	private List<Layout> list_manager;

	public LayoutManagerHelper() {
		list_manager = new ArrayList<Layout>();
	}

	public Layer getFirstSpriteByTag(int tag) {
		if (list_manager != null && !list_manager.isEmpty()) {
			for (Layout l : list_manager) {
				if (l.layer.getTag() == tag)
					return l.layer;
			}
		}
		return null;
	}

	public void add(Layer l) {
		list_manager.add(new Layout(l, 0));
	}

	public void add(Layer l, int zindex) {
		list_manager.add(new Layout(l, zindex));
	}

	public void remove(SpriteHelper s) {
		Layout tmp = null;
		for (Layout l : list_manager) {
			if (l.layer == s) {
				tmp = l;
				break;
			}
		}
		if (tmp != null) {
			list_manager.remove(tmp);
		}
	}

	public void onDraw(Canvas canvas) {
		Collections.sort(list_manager,
				LayoutComparator.getComparator(LayoutComparator.ZINDEX_SORT));
		for (Layout l : list_manager) {
			l.layer.onDraw(canvas);
		}
	}

	public void update() {
		for (Layout l : list_manager) {
			l.layer.update();
		}
	}

	public void release() {
		for (Layout l : list_manager) {
			l.layer.release();
		}
	}

	public class Tuple<E, F, G> {
		public E First;
		public F Second;
	}

	private class Layout {

		public Layer layer;
		public int zindex;

		public Layout(Layer s, int zindex) {
			this.layer = s;
			this.zindex = zindex;
		}
	}

	enum LayoutComparator implements Comparator<Layout> {
		ZINDEX_SORT {
			public int compare(Layout o1, Layout o2) {
				return Integer.valueOf(o1.zindex).compareTo(o2.zindex);
			}
		};

		public static Comparator<Layout> decending(
				final Comparator<Layout> other) {
			return new Comparator<Layout>() {
				public int compare(Layout o1, Layout o2) {
					return -1 * other.compare(o1, o2);
				}
			};
		}

		public static Comparator<Layout> getComparator(
				final LayoutComparator... multipleOptions) {
			return new Comparator<Layout>() {
				public int compare(Layout o1, Layout o2) {
					for (LayoutComparator option : multipleOptions) {
						int result = option.compare(o1, o2);
						if (result != 0) {
							return result;
						}
					}
					return 0;
				}
			};
		}
	}
}