package opa.datafilter.core.ast.db.query;

import opa.datafilter.core.ast.db.query.elements.SqlPredicate;
import opa.datafilter.core.ast.db.query.model.response.Predicate;
import opa.datafilter.core.ast.db.query.model.response.Term;
import opa.datafilter.core.ast.db.query.model.response.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static opa.datafilter.core.ast.db.query.model.OpaConstants.*;

/**
 * @author joffryferrater
 *
 * Converts Open Policy Agent Abstract Syntax Tree predicate into SQL predicate.
 */
public class PredicateConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PredicateConverter.class);

    private Predicate predicate;

    public PredicateConverter(Predicate predicate) {
        this.predicate = predicate;
    }

    public SqlPredicate astToSqlPredicate() {
        SqlPredicate sqlPredicate = new SqlPredicate();
        List<Term> terms = predicate.getTerms();
        Term operator = terms.get(0);
        String operatorString = getOperator(operator);
        sqlPredicate.setOperator(operatorString);
        Object termValue1 = terms.get(1).getValue();
        Object termValue2 = terms.get(2).getValue();
        if(termValue1 instanceof ArrayList) {
            String left = (String)getExpression(termValue1);
            sqlPredicate.setLeftExpression(left);
            Object right = getExpression(termValue2);
            sqlPredicate.setRightExpression(right);
        } else {
            String left = (String)getExpression(termValue2);
            sqlPredicate.setLeftExpression(left);
            Object right = getExpression(termValue1);
            sqlPredicate.setRightExpression(right);
        }
        String toString = sqlPredicate.toString();
        LOGGER.info("SQL predicate: {}", toString);
        return sqlPredicate;
    }

    Object getExpression(Object termValue) {
        Object expression = "";
        if (termValue instanceof ArrayList) {
            List<Value> valueList = (List<Value>) termValue;
            Value firstValue = valueList.get(1);
            Value secondValue = valueList.get(3);
            expression = firstValue.getValues() + "." + secondValue.getValues();
        } else if (termValue instanceof String) {
            expression = termValue;
        } else if (termValue instanceof Integer) {
            expression = String.valueOf(termValue);
        } else if(termValue instanceof Long) {
            expression = String.valueOf(termValue);
        } else {
            LOGGER.warn("Unknown value type");
        }
        return expression;
    }

    String getOperator(Term term) {
        Object termValue = term.getValue();
        List<Value> valueList;
        Value valueObject;
        if (termValue instanceof ArrayList) {
            valueList = (List<Value>) termValue;
            valueObject = valueList.get(0);
        } else {
            valueObject = (Value) termValue;
        }
        String value = (String) valueObject.getValues();
        return getOperatorFromValue(value);
    }

    String getOperatorFromValue(String value) {
        String operator = null;
        switch (value) {
            case EQ:
                operator = "=";
                break;
            case GT:
                operator = ">";
                break;
            case GTE:
                operator = ">=";
                break;
            case LT:
                operator = "<";
                break;
            case LTE:
                operator = "<=";
                break;
            default:
                LOGGER.warn("Operator '{}' is not yet supported", value);
        }
        return operator;
    }
}

