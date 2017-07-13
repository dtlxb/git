package cn.gogoal.im.bean.stock;

import java.util.ArrayList;
import java.util.List;


public class KDJBean {
	private ArrayList<Double> Ks;
	private ArrayList<Double> Ds;
	private ArrayList<Double> Js;

	public KDJBean(List<OHLCData> OHLCData) {
		Ks = new ArrayList<Double>();
		Ds = new ArrayList<Double>();
		Js = new ArrayList<Double>();

		ArrayList<Double> ks = new ArrayList<Double>();
		ArrayList<Double> ds = new ArrayList<Double>();
		ArrayList<Double> js = new ArrayList<Double>();

		double k = 0.0;
		double d = 0.0;
		double j = 0.0;
		double rSV = 0.0;

		if (OHLCData != null && OHLCData.size() > 0) {

			cn.gogoal.im.bean.stock.OHLCData oHLCData = OHLCData.get(OHLCData.size() - 1);
			double high = oHLCData.getHigh_price();
			double low = oHLCData.getLow_price();

			for (int i = OHLCData.size() - 1; i >= 0; i--) {
				if (i < OHLCData.size() - 1) {
					oHLCData = OHLCData.get(i);
					high = high > oHLCData.getHigh_price() ? high : oHLCData.getHigh_price();
					low = low < oHLCData.getLow_price() ? low : oHLCData.getLow_price();
				}
				if (high != low) {
					rSV = (oHLCData.getClose_price() - low) / (high - low) * 100;
				}
				if (i == OHLCData.size() - 1) {
					k = rSV;
					d = k;

				} else {
					k = k * 2 / 3 + rSV / 3;
					d = d * 2 / 3 + k / 3;
				}
				j = 3 * k - 2 * d;

				ks.add(k);
				ds.add(d);
				js.add(j);
			}
			for (int i = ks.size() - 1; i >= 0; i--) {
				Ks.add(ks.get(i));
				Ds.add(ds.get(i));
				Js.add(js.get(i));
			}
		}
	}

	public ArrayList<Double> getK() {
		return Ks;
	}

	public ArrayList<Double> getD() {
		return Ds;
	}

	public ArrayList<Double> getJ() {
		return Js;
	}
}
