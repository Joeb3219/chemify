package com.charredgames.chemify.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.charredgames.chemify.ExpandedListAdapter;
import com.charredgames.chemify.ExpandedListGroup;
import com.charredgames.chemify.R;
import com.charredgames.chemify.activity.ActivitySeriesActivity;
import com.charredgames.chemify.activity.ConstantsActivity;
import com.charredgames.chemify.activity.DefinitionsActivity;
import com.charredgames.chemify.activity.PHConverter;
import com.charredgames.chemify.activity.PolyIonsActivity;
import com.charredgames.chemify.activity.ProblemInput;
import com.charredgames.chemify.activity.SendPost;
import com.charredgames.chemify.constant.Definition;
import com.charredgames.chemify.constant.Ion;
import com.charredgames.chemify.constant.Prefix;
import com.charredgames.chemify.constant.ProblemGuts;
import com.charredgames.chemify.constant.Unit;
import com.charredgames.chemify.constant.UnitPrefix;
import com.charredgames.chemify.constant.UnitType;
import com.charredgames.chemify.problems.Problem;
import com.charredgames.chemify.problems.ResponseType;
import com.charredgames.chemify.problems.UnitConverter;
import com.charredgames.chemify.problems.WavelengthConverter;

/**
 * @author Joe Boyle <joe@charredgames.com>
 * @since Nov 17, 2013
 */

public class Controller {

	public static final String _VERSION = "1.4.4";
	private static AssetManager assets = null;
	public static ArrayList<ResponseType> types = new ArrayList<ResponseType>();
	public static ArrayList<Prefix> prefixes = new ArrayList<Prefix>();
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	public static ArrayList<String> conversionSymbols = new ArrayList<String>(Arrays.asList("converted to", "equals"));
	public static Map<String, ProblemGuts> problemTypes = new TreeMap<String, ProblemGuts>();
	public static SparseArray<String> romanNumerals = new SparseArray<String>();
	public static ArrayList<UnitPrefix> unitPrefixes = new ArrayList<UnitPrefix>();
	public static ArrayList<UnitType> unitTypes = new ArrayList<UnitType>();
	public static final Locale _LOCALE = Locale.getDefault();
	public static boolean autoFormat = true, calculateReasoning = true, sendUsage = false;
	public static Map<String, String> reactionSymbols = new HashMap<String, String>();
	public static final String _GENERIC_PLUS_SIGN = "+", _GENERIC_YIELDS_SIGN = ">";
	public static Context context;
	public static Resources resources;
	
	public static void resetIfNeeded(Context c){
		if(context == null) context = c;
		if(resources == null) resources = context.getResources();
		if(Ion.ions.size() == 0) reset(context.getAssets());
	}
	
	public static void reset(AssetManager aManager){
		boolean firstRun = false;
		if(assets == null) firstRun = true;
		assets = aManager;
		resources = context.getResources();
		setElements("/default/elements.cgf");
		Ion.ions = new ArrayList<Ion>();
		setIons("/default/polyions.cgf");
		Definition.definitions = new ArrayList<Definition>();
		setDefinitions("/default/definitions.cgf");
		
		if(ResponseType.answer instanceof ResponseType);
		if(UnitPrefix.ATTO instanceof UnitPrefix);
		if(Prefix.mono instanceof Prefix);
		if(Ion.ions.size() > 4);
		if(Unit.ACRE instanceof Unit);
		if(Controller.units.size() > 1);
		if(UnitType.ELECTRICAL_CHARGE instanceof UnitType);
		if(UnitPrefix.ATTO instanceof UnitPrefix);
		if(com.charredgames.chemify.constant.Element.elements.size() > 3);
		
		romanNumerals.put(1, "I");
		romanNumerals.put(2, "II");
		romanNumerals.put(3, "III");
		romanNumerals.put(4, "IV");
		romanNumerals.put(5, "V");
		romanNumerals.put(6, "VI");
		romanNumerals.put(7, "VII");
		romanNumerals.put(8, "VIII");
		romanNumerals.put(9, "IX");
		romanNumerals.put(10, "X");
		romanNumerals.put(11, "XI");
		romanNumerals.put(12, "XII");
		romanNumerals.put(13, "XIII");
		romanNumerals.put(14, "XIV");
		romanNumerals.put(15, "XV");
		
		reactionSymbols.put("+", _GENERIC_PLUS_SIGN);
		//reactionSymbols.put("with", "");
		//reactionSymbols.put("reacts", _GENERIC_PLUS_SIGN);
		reactionSymbols.put(">", _GENERIC_YIELDS_SIGN);
		reactionSymbols.put("->", _GENERIC_YIELDS_SIGN);
		reactionSymbols.put("=>", _GENERIC_YIELDS_SIGN);
		reactionSymbols.put("&#8652;", _GENERIC_YIELDS_SIGN);
		reactionSymbols.put("yields", _GENERIC_YIELDS_SIGN);
		
		if(!firstRun) return;
		
		problemTypes.put(resources.getString(R.string.problem_nomenclature), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				View view = LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
				((EditText) view.findViewById(R.id.edit_input)).setHint(resources.getString(R.string.nomenclature_hint));
				return view;
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_basic))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_molar_mass), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				View view = LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
				((EditText) view.findViewById(R.id.edit_input)).setHint(resources.getString(R.string.weight_hint));
				return view;			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_basic))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_reactions), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				View view = LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
				((EditText) view.findViewById(R.id.edit_input)).setHint(resources.getString(R.string.reactions_hint));
				return view;			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_basic))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_element_info), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				View view = LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
				((EditText) view.findViewById(R.id.edit_input)).setHint(resources.getString(R.string.element_info_hint));
				return view;			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_basic))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_solubility), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				View view = LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
				((EditText) view.findViewById(R.id.edit_input)).setHint(resources.getString(R.string.solubility_hint));
				return view;			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_basic))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_oxdidation), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				View view = LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
				((EditText) view.findViewById(R.id.edit_input)).setHint(resources.getString(R.string.oxidation_hint));
				return view;			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_basic))) return true;
				return false;
			}});
		/*problemTypes.put(resources.getString(R.string.problem_dimensional_analysis), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_basic))) return true;
				return false;
			}});*/
		problemTypes.put(resources.getString(R.string.problem_balancer), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_basic))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_stoichiometry), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_converters))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_unit_converter), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, UnitConverter.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_converters))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_ph_converter), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, PHConverter.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_converters))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_half_life), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_nuclear))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_waves), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, WavelengthConverter.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.wavelengthconverter, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_converters))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_boyle_law), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_gas_laws))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_charles_law), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_gas_laws))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_gaylussac_law), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_gas_laws))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_ideal_law), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_gas_laws))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_combined_law), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_gas_laws))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_entropy), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_thermodynamics))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_calorimetry), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_thermodynamics))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_gibbs), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_thermodynamics))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_specific_heat), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_thermodynamics))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_rate), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_kinetics))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_order), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_kinetics))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_activation_energy), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_kinetics))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_k), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_equilibrium))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_buffer_capacity), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_equilibrium))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_half_cell), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_electrochemistry))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_cell_potential), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_electrochemistry))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_nuclear_something), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_nuclear))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.problem_organic_stuff), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_organic))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_quizzes), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_dictionary), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, DefinitionsActivity.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_periodic_table), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_activity_series), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ActivitySeriesActivity.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_standard_reduction), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_solubility_rules), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_constants), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ConstantsActivity.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_polyatomics), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, PolyIonsActivity.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_formulas), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
		problemTypes.put(resources.getString(R.string.reference_units), new ProblemGuts(){
			public void openActivity(Context context){context.startActivity(new Intent(context, ProblemInput.class));}
			public View getInputView(Context context) {
				return LayoutInflater.from(context).inflate(R.layout.generic_problem_input, null);
			}
			public boolean getSubmit(Context context, View view) {
				return true;
			}
			public boolean fitsCategory(String cat) {
				if(cat.equals(resources.getString(R.string.group_reference))) return true;
				return false;
			}});
	}
	
	public static ExpandedListAdapter getMainGroupsAdapter(Context context){
		ArrayList<ExpandedListGroup> groups = new ArrayList<ExpandedListGroup>();
		ArrayList<String> items = new ArrayList<String>();
		
		ExpandedListGroup g = new ExpandedListGroup(resources.getString(R.string.group_basic), items);
		populateGroup(resources.getString(R.string.group_basic), items);
		groups.add(g);
		items = new ArrayList<String>();
		g = new ExpandedListGroup(resources.getString(R.string.group_converters), items);
		populateGroup(resources.getString(R.string.group_converters), items);
		groups.add(g);
		items = new ArrayList<String>();
		g = new ExpandedListGroup(resources.getString(R.string.group_gas_laws), items);
		groups.add(g);
		populateGroup(resources.getString(R.string.group_gas_laws), items);
		items = new ArrayList<String>();
		g = new ExpandedListGroup(resources.getString(R.string.group_thermodynamics), items);
		groups.add(g);
		populateGroup(resources.getString(R.string.group_thermodynamics), items);
		items = new ArrayList<String>();
		g = new ExpandedListGroup(resources.getString(R.string.group_kinetics), items);
		groups.add(g);
		populateGroup(resources.getString(R.string.group_kinetics), items);
		items = new ArrayList<String>();
		g = new ExpandedListGroup(resources.getString(R.string.group_equilibrium), items);
		groups.add(g);
		populateGroup(resources.getString(R.string.group_equilibrium), items);
		items = new ArrayList<String>();
		g = new ExpandedListGroup(resources.getString(R.string.group_electrochemistry), items);
		groups.add(g);
		populateGroup(resources.getString(R.string.group_electrochemistry), items);
		items = new ArrayList<String>();
		g = new ExpandedListGroup(resources.getString(R.string.group_nuclear), items);
		groups.add(g);
		populateGroup(resources.getString(R.string.group_nuclear), items);
		items = new ArrayList<String>();
		g = new ExpandedListGroup(resources.getString(R.string.group_organic), items);
		groups.add(g);
		populateGroup(resources.getString(R.string.group_organic), items);
		items = new ArrayList<String>();
		g = new ExpandedListGroup(resources.getString(R.string.group_reference), items);
		groups.add(g);
		populateGroup(resources.getString(R.string.group_reference), items);
		
		return new ExpandedListAdapter(context, groups);
	}
	
	private static void populateGroup(String category, ArrayList<String> items){
		for(Entry<String, ProblemGuts> entry : problemTypes.entrySet()){
			if(entry.getValue().fitsCategory(category)) items.add(entry.getKey());
		}
	}

	public static String replaceReactionSymbols(String str){
		for(Entry<String, String> entry : reactionSymbols.entrySet()){
			String key = entry.getKey();
			if(str.contains(key)) str = str.replace(key, entry.getValue());
			if(str.contains(" " + key)) str = str.replace(" " + key, entry.getValue());
			if(str.contains(key + " ")) str = str.replace(key + " ", entry.getValue());
			if(str.contains(" " + key + " ")) str = str.replace(" " + key + " ", entry.getValue());
		}
		
		return str;
	}
	
	public static String replaceConversionSymbols(String str){
		for(String s : conversionSymbols){
			if(str.contains(s)) str = str.replace(s, "to");
		}
		str = str.replace("of", "");
		return str;
	}
	
	public static String convertIntToNumeral(int num){
		if(romanNumerals.get(num) != null) return romanNumerals.get(num);
		return "I";
	}
	
	public static int convertNumeralToInt(String str){
		if(romanNumerals.indexOfValue(str) > 0){
			return romanNumerals.keyAt(romanNumerals.indexOfValue(str));
		}else{
			try{
				Integer.parseInt(str);
			}catch(Exception e){
				return 1;
			}
			return Integer.parseInt(str);
		}
	}
	
	private static void setElements(String path){
		if(assets == null) return;
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = null;
		try {
			xmlFile = getFileFromStream("elements", assets.open("default/elements.cgf"));
			
		try {
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			//Handles loading world chests
			List<Element> list = rootNode.getChildren("element");
			for (int i = 0; i < list.size(); i++) {				
				Element ele = (Element) list.get(i);
				com.charredgames.chemify.constant.Element element = com.charredgames.chemify.constant.Element.getElement(ele.getAttributeValue("name"));
				element.setInfo(ele.getChild("string").getAttributeValue("symbol"), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("number")), 
						Double.parseDouble(ele.getChild("int").getAttributeValue("mass")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("period")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("group")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("charge")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("valence")), 
						Integer.parseInt(ele.getChild("int").getAttributeValue("activity")));
				
			}
		  } catch (IOException e) {e.printStackTrace();} catch (JDOMException e) {e.printStackTrace();  }} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			if(xmlFile != null) xmlFile.delete();
		}
	}
	
	private static void setIons(String path){
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = null;
		try {
			xmlFile = getFileFromStream("polyions", assets.open("default/polyions.cgf"));
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			List<Element> list = rootNode.getChildren("ion");
			for (int i = 0; i < list.size(); i++) {				
				Element ionItem = (Element) list.get(i);
				new Ion(
						ionItem.getChild("elements").getAttributeValue("list"),
						ionItem.getChild("string").getAttributeValue("name"),
						Integer.parseInt(ionItem.getChild("int").getAttributeValue("charge"))
						);
			}
		} catch (IOException e) {e.printStackTrace();} catch (JDOMException e) {e.printStackTrace();  }
		finally{
			if(xmlFile != null) xmlFile.delete();
		}
	}
	
	private static void setDefinitions(String path){
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = null;
		try {
			xmlFile = getFileFromStream("definitions", assets.open("default/definitions.cgf"));
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			List<Element> list = rootNode.getChildren("entry");
			for (int i = 0; i < list.size(); i++) {				
				Element definition = (Element) list.get(i);
				Definition d = new Definition(
						definition.getChildText("word"),
						definition.getChildText("definition"));
				if(definition.getChildText("example") != null) d.setExample(definition.getChildText("example"));
			}
		} catch (IOException e) {e.printStackTrace();} catch (JDOMException e) {e.printStackTrace();  }
		finally{
			if(xmlFile != null) xmlFile.delete();
		}
	}
	
	private static File getFileFromStream(String prefix, InputStream in){
		File file = null;
		try{
			file = File.createTempFile(prefix, ".cgf", context.getCacheDir());
			file.deleteOnExit();
			OutputStream os = new FileOutputStream(file);
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = in.read(buffer)) !=-1){
                os.write(buffer, 0, bytesRead);
            }
            in.close();
            os.flush();
            os.close();
		}catch(Exception e){e.printStackTrace();}
		if(file == null) file = new File("p");
		return file;
	}
	
	public static Ion getIon(String str){
		for(Ion ion : Ion.ions){
			if(ion.getElementString().equals(str)) return ion;
		}
		return Ion.ions.get(0);
	}

	public static String stripHtmlTags(String str){
		if(str.contains("<sub>")) str = str.replace("<sub>", "");
		if(str.contains("</sub>")) str = str.replace("</sub>", "");
		if(str.contains("<sup>")) str = str.replace("<sup>", "");
		if(str.contains("</sup>")) str = str.replace("</sup>", "");
		if(str.contains("<br>")) str = str.replace("<br>", "");
		return str;
	}
	
	public static void reloadSettings(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		autoFormat = prefs.getBoolean("pref_auto-format", true);
		calculateReasoning = prefs.getBoolean("pref_calculate-reasoning", true);
		sendUsage = prefs.getBoolean("pref_send-data", false);
	}
	
	public static int getGCD(int a, int b){
		if(a == 0 || b == 0) return a+b;
		return getGCD(b, a%b);
	}
	
	public static int getGCD(ArrayList<Integer> list){
		if(list.size() == 1) return 1;
		int result = list.get(0);
		
		for(int i = 1; i < list.size(); i ++) result = getGCD(result, list.get(i));
		
		return result;
	}

	public static String normalizeString(String string, boolean upper){
		String str = string.toLowerCase(_LOCALE);
		if(upper) return str.substring(0,1).toUpperCase(_LOCALE) + str.substring(1);
		return str;
	}
	
	public static void sendUsageReport(String operation, Problem problem){
		HttpClient httpclient = new DefaultHttpClient();
  		 HttpPost httppost = new HttpPost("http://www.charredgames.com/usagedata.php");
  		    
  		 try {
  			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
  			nameValuePairs.add(new BasicNameValuePair("version", Controller._VERSION));
  			nameValuePairs.add(new BasicNameValuePair("problem", operation));
  			nameValuePairs.add(new BasicNameValuePair("input", problem.getInput()));
  			String r = "";
  			if(problem.getResponse() != null) r = problem.getResponse().getResponse();
  			nameValuePairs.add(new BasicNameValuePair("output", r));
  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
  			
  			new SendPost(httpclient, httppost).execute();

  		} catch (IOException e) {}
	}
	
	public static String doubleToScientific(Double num){
		DecimalFormat f = new DecimalFormat("0.###E0");
		String formatted = "";
		if(num <= 9999 && num >= -9999 && (num >= 0.001 || num <= -0.001 || num == 0)){
			f = new DecimalFormat("0.000");
			formatted = f.format(num);
		}else{
			formatted = f.format(num);
			formatted = formatted.replace("E", " * 10<sup>");
			formatted += "</sup>";
		}
		return formatted;
	}
	
	public static double checkInfinityValues(double val){
		if(Double.isInfinite(val)) return 0.00;
		if(val > Double.MAX_VALUE) return Double.MAX_VALUE;
		if(val < Double.MIN_VALUE) return Double.MIN_VALUE;
		
		return val;
	}
	
}
