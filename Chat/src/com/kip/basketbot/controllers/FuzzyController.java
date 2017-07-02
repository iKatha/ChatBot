package com.kip.basketbot.controllers;

import com.fuzzylite.Engine;
import com.fuzzylite.FuzzyLite;
import com.fuzzylite.Op;
import com.fuzzylite.activation.General;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.EinsteinSum;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.AlgebraicProduct;
import com.fuzzylite.norm.t.EinsteinProduct;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Bell;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

public class FuzzyController {
	
	private String p_assists;
	private String p_points;
	private String p_rebounds;
	Engine engine;
	
	/**
	 * 
	 * @param k_asysty asysty
	 * @param k_punkty punkty 
	 * @param k_zbiorki zbiórki
	 */
	public FuzzyController(String k_asysty, String k_punkty, String k_zbiorki) {
		engine = new Engine();
        engine.setName("FuzzyController");
        engine.setDescription("");
		this.p_assists = k_asysty;
		this.p_points = k_punkty;
		this.p_rebounds = k_zbiorki;
	}
	
	public String playerStats() {
		String response="";	
		
		InputVariable assists = new InputVariable();
		assists.setName("assists");
		assists.setDescription("");
		assists.setEnabled(true);
		assists.setRange(0.000, 15.000);
		assists.setLockValueInRange(true);
		assists.addTerm(new Bell("bad",5,5,10));
		assists.addTerm(new Bell("average",7.5,5,10));
		assists.addTerm(new Bell("good",12,5,10));
        engine.addInputVariable(assists);
       
        InputVariable rebounds = new InputVariable();
        rebounds.setName("rebounds");
        rebounds.setDescription("");
        rebounds.setEnabled(true);
        rebounds.setRange(0.000, 20.000);
        rebounds.setLockValueInRange(true);
        rebounds.addTerm(new Bell("bad",7,7,10));
        rebounds.addTerm(new Bell("average",10,7,10));
        rebounds.addTerm(new Bell("good",13,7,10));
        engine.addInputVariable(rebounds);
        
        InputVariable points = new InputVariable();
        points.setName("points");
        points.setDescription("");
        points.setEnabled(true);
        points.setRange(0.000, 50.000);
        points.setLockValueInRange(true);
        points.addTerm(new Bell("bad",10,15,10));
        points.addTerm(new Bell("average",10,15,10));
        points.addTerm(new Bell("good",40,15,10));
        engine.addInputVariable(points);
            
        OutputVariable opinion = new OutputVariable();
        opinion.setName("opinion");
        opinion.setDescription("");
        opinion.setEnabled(true);
        opinion.setRange(0.000, 10.000);
        opinion.setLockValueInRange(true);
        opinion.setAggregation(new Maximum());
        opinion.setDefuzzifier(new Centroid(100));
        opinion.setLockPreviousValue(false);
        opinion.addTerm(new Bell("bad",0,3,25));
        opinion.addTerm(new Bell("average",5,5,25));
        opinion.addTerm(new Bell("good",10,3,25));
        engine.addOutputVariable(opinion);
        
        
        RuleBlock mamdani = new RuleBlock();
        mamdani.setName("mamdani");
        mamdani.setDescription("");
        mamdani.setEnabled(true);
        mamdani.setConjunction(new EinsteinProduct());
        mamdani.setDisjunction(new EinsteinSum());

        mamdani.setImplication(new AlgebraicProduct());
        mamdani.setActivation(new General());
      

        
        mamdani.addRule(Rule.parse("if assists is bad and rebounds is bad and points is bad then opinion is very bad with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is bad and rebounds is bad and points is average then opinion is bad with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is bad and rebounds is bad and points is good then opinion is average with 0.7", engine));
        
        mamdani.addRule(Rule.parse("if assists is bad and rebounds is average and points is bad then opinion is bad with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is bad and rebounds is average and points is average then opinion is average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is bad and rebounds is average and points is good then opinion is somewhat good with 0.7", engine));
        
        mamdani.addRule(Rule.parse("if assists is bad and rebounds is good and points is bad then opinion is somewhat average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is bad and rebounds is good and points is average then opinion is average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is bad and rebounds is good and points is good then opinion is good with 0.7", engine));
        
        mamdani.addRule(Rule.parse("if assists is average and rebounds is bad and points is bad then opinion is bad with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is average and rebounds is bad and points is average then opinion is average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is average and rebounds is bad and points is good then opinion is somewhat good with 0.7", engine));
        
        mamdani.addRule(Rule.parse("if assists is average and rebounds is average and points is bad then opinion is somewhat average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is average and rebounds is average and points is average then opinion is average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is average and rebounds is average and points is good then opinion is good with 0.7", engine));
        
        mamdani.addRule(Rule.parse("if assists is average and rebounds is good and points is bad then opinion is average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is average and rebounds is good and points is average then opinion is good with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is average and rebounds is good and points is good then opinion is very good with 0.7", engine));
        
        mamdani.addRule(Rule.parse("if assists is good and rebounds is bad and points is bad then opinion is somewhat average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is good and rebounds is bad and points is average then opinion is average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is good and rebounds is bad and points is good then opinion is good with 0.7", engine));
        
        mamdani.addRule(Rule.parse("if assists is good and rebounds is average and points is bad then opinion is average with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is good and rebounds is average and points is average then opinion is somewhat good with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is good and rebounds is average and points is good then opinion is very good with 0.7", engine));
        
        mamdani.addRule(Rule.parse("if assists is good and rebounds is good and points is bad then opinion is good with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is good and rebounds is good and points is average then opinion is very good with 0.7", engine));
        mamdani.addRule(Rule.parse("if assists is good and rebounds is good and points is good then opinion is very very good with 0.7", engine));
        
        mamdani.addRule(Rule.parse("if points is very good then opinion is very good with 1.0", engine));
       
        engine.addRuleBlock(mamdani);


        StringBuilder status = new StringBuilder();
        if (! engine.isReady(status))
            throw new RuntimeException("[engine error] engine is not ready:n" + status);

        InputVariable pointsInputVar = engine.getInputVariable("points");
        InputVariable reboundsInputVar = engine.getInputVariable("rebounds");
        InputVariable assistsInputVar = engine.getInputVariable("assists");
        
        OutputVariable opp = engine.getOutputVariable("opinion");

        
        pointsInputVar.setValue(Double.parseDouble(p_points));
        assistsInputVar.setValue(Double.parseDouble(p_assists));
        reboundsInputVar.setValue(Double.parseDouble(p_rebounds));
        engine.process();
//            FuzzyLite.logger().info(String.format(
//                    "points.input = %s and assists.input = %s  and rebounds.input = %s -> opinion.output = %s",
//                    Op.str(Double.parseDouble(p_points)),Op.str(Double.parseDouble(p_assists)),Op.str(Double.parseDouble(p_rebounds)), Op.str(opp.getValue())));
//            
        response=Op.str(opp.getValue());
		return response;
	}

	
}