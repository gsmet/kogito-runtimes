package org.drools.base.evaluators;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import org.drools.base.BaseEvaluator;
import org.drools.spi.Evaluator;

/**
 * This is the misc "bucket" evaluator factory for objects.
 * It is fairly limited in operations, 
 * and what operations are available are dependent on the exact type.
 * 
 * This supports "<" and ">" etc by requiring objects to implement the comparable interface.
 * Of course, literals will not work with comparator, as it has no way
 * of converting from literal to the appropriate type.
 * 
 * @author Michael Neale
 */
public class ObjectFactory {

    public static Evaluator getObjectEvaluator(int operator) {
        switch ( operator ) {
            case Evaluator.EQUAL :
                return ObjectEqualEvaluator.INSTANCE;
            case Evaluator.NOT_EQUAL :
                return ObjectNotEqualEvaluator.INSTANCE;
            case Evaluator.CONTAINS :
                return ObjectContainsEvaluator.INSTANCE;
            case Evaluator.LESS :
                return ObjectLessEvaluator.INSTANCE;
            case Evaluator.LESS_OR_EQUAL :
                return ObjectLessOrEqualEvaluator.INSTANCE;
            case Evaluator.GREATER :
                return ObjectGreaterEvaluator.INSTANCE;
            case Evaluator.GREATER_OR_EQUAL :
                return ObjectGreaterOrEqualEvaluator.INSTANCE;
            case Evaluator.EXCLUDES :
                return ObjectExcludesEvaluator.INSTANCE;
            default :
                throw new RuntimeException( "Operator '" + operator + "' does not exist for ObjectEvaluator" );
        }
    }

    static class ObjectEqualEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new ObjectEqualEvaluator();

        private ObjectEqualEvaluator() {
            super( Evaluator.OBJECT_TYPE,
                   Evaluator.EQUAL );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            if ( object1 == null ) return object2 == null;
            return object1.equals( object2 );
        }

        public String toString() {
            return "Object ==";
        }
    }

    static class ObjectNotEqualEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new ObjectNotEqualEvaluator();

        private ObjectNotEqualEvaluator() {
            super( Evaluator.OBJECT_TYPE,
                   Evaluator.NOT_EQUAL );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            if ( object1 == null ) return !(object2 == null);
            return !object1.equals( object2 );
        }

        public String toString() {
            return "Object !=";
        }
    }

    static class ObjectLessEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new ObjectLessEvaluator();

        private ObjectLessEvaluator() {
            super( Evaluator.OBJECT_TYPE,
                   Evaluator.LESS );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            Comparable comp = (Comparable) object1;
            int val = comp.compareTo( object2 );
            return val < 0;
        }

        public String toString() {
            return "Object <";
        }
    }

    static class ObjectLessOrEqualEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new ObjectLessOrEqualEvaluator();

        private ObjectLessOrEqualEvaluator() {
            super( Evaluator.OBJECT_TYPE,
                   Evaluator.LESS_OR_EQUAL );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            Comparable comp = (Comparable) object1;
            return comp.compareTo( object2 ) <= 0;
        }

        public String toString() {
            return "Object <=";
        }
    }

    static class ObjectGreaterEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new ObjectGreaterEvaluator();

        private ObjectGreaterEvaluator() {
            super( Evaluator.OBJECT_TYPE,
                   Evaluator.GREATER );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            Comparable comp = (Comparable) object1;
            return comp.compareTo( object2 ) > 0;
        }

        public String toString() {
            return "Object >";
        }
    }

    static class ObjectGreaterOrEqualEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new ObjectGreaterOrEqualEvaluator();

        private ObjectGreaterOrEqualEvaluator() {
            super( Evaluator.OBJECT_TYPE,
                   Evaluator.GREATER_OR_EQUAL );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            Comparable comp = (Comparable) object1;
            return comp.compareTo( object2 ) >= 0;
        }

        public String toString() {
            return "Object >=";
        }
    }

    static class ObjectContainsEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new ObjectContainsEvaluator();

        private ObjectContainsEvaluator() {
            super( Evaluator.OBJECT_TYPE,
                   Evaluator.CONTAINS );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            if ( object2 == null ) return false;
            Collection col = (Collection) object1;
            return col.contains( object2 );
        }

        public String toString() {
            return "Object contains";
        }
    }
    
    static class ObjectExcludesEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new ObjectExcludesEvaluator();

        private ObjectExcludesEvaluator() {
            super( Evaluator.OBJECT_TYPE,
                   Evaluator.EXCLUDES );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            if ( object2 == null ) return false;
            Collection col = (Collection) object1;
            return !col.contains( object2 );
        }

        public String toString() {
            return "Object excludes";
        }
    }    

}