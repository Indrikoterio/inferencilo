/**
 * KnowledgeBase
 *
 * Defines a dictionary of Prolog rules and facts.
 * The dictionary (hashmap) is indexed by predicate
 * name and arity, eg. mother/2
 *
 * @author  Klivo
 * @version 1.0
 */

package inferencilo;

import java.util.*;

public class KnowledgeBase {

   private HashMap<String, List<Rule>> rules = new HashMap<>();

   /*
    * constructor
    *
    * @param  array of rules/facts
    */
   public KnowledgeBase(Rule... roolz) {
      for (Rule rule : roolz) {
         addRule(rule);
      }
   }

   /**
    * addRule
    *
    * Add one new rule to the knowledge base.
    *
    * @param rule object
    */
   public void addRule(Rule rule) {
      String key = rule.key();
      List<Rule> list = rules.get(key);
      if (list == null) {
         list = new ArrayList<Rule>();
         list.add(rule);
         rules.put(key, list);
      }
      else {
         list.add(rule);
      }
   }

   /**
    * addRule
    *
    * Add a fact to the knowledge base.
    *
    * @param fact as string, eg: "boss(Beth, Lily)"
    */
   public void addRule(String str) {
      addRule(new Rule(str));
   }

   /**
    * addFact
    *
    * Add a fact to the knowledge base.
    *
    * @param fact as Complex term.
    */
   public void addFact(Complex c) {
      addRule(new Rule(c));
      //showKB();
   }


   /**
    * addRules
    *
    * Add facts to the knowledge base.
    *
    * @param  list of rule strings
    */
   public void addRules(List<String> ruleList) {
      for (String str : ruleList) {
         addRule(new Rule(str));
      }
   }


   /**
    * remove
    *
    * Removes the indicated rule/fact from the knowledge base.
    *
    * @param rule as complex term.
    */
   public void remove(Complex c) { rules.remove(c.key()); }

   /**
    * remove
    *
    * Removes the indicated rule/fact from the knowledge base.
    *
    * @param rule as string (eg.: "father/2")
    */
   public void remove(String str) { rules.remove(str); }


   /**
    * getRuleStandardizedApart
    *
    * Get a rule (or fact) from the Knowledge Base. Rules are
    * indexed by functor/arity (eg. sister/2) and by index number.
    *
    * The retrieved rule is 'standardized', meaning that the
    * variables are made unique.
    *
    * @param  goal
    * @param  index
    * @return rule/fact
    */
   public Rule getRuleStandardizedApart(Goal goal, int i) {
      String key = ((Complex)goal).key();
      List<Rule> list = rules.get(key);
      Rule rule = list.get(i);
      rule = (Rule)rule.standardizeVariablesApart(
                new HashMap<String, LogicVar>()
             );
      return rule;
   }

   /**
    * getRuleCount
    *
    * Counts the number of rules for a given predicate.
    *
    * @param  goal
    * @return count
    */
   public int getRuleCount(Goal goal) {
      //showKB();
      String key = ((Complex)goal).key();
      List<Rule> list = rules.get(key);
      if (list == null) {
         //System.out.println("\nWarning - Unknown rule: " + key);
         return 0;
      }
      else {
         return list.size();
      }
   }

   /**
    * getRuleCount
    *
    * Counts the total number of rules in the knowledge base.
    *
    * @return count
    */
   public int getRuleCount() {
      return rules.size();
   }

   /**
    * showKB ()
    *
    * For diagnostic purposes.
    */
   public void showKB() {
      System.out.println("########## Contents of Knowledge Base ##########");
      for (String key : rules.keySet()) {
         List<Rule> list = rules.get(key);
         for (Rule r : list) {
            System.out.println("rule: " + r);
         }
      }
   } // showKB

}  // KnowledgeBase
