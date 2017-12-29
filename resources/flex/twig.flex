package com.fisk.twig.parsing;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;

// suppress various warnings/inspections for the generated class
@SuppressWarnings ({"FieldCanBeLocal", "UnusedDeclaration", "UnusedAssignment", "AccessStaticViaInstance", "MismatchedReadAndWriteOfArray", "WeakerAccess", "SameParameterValue", "CanBeFinal", "SameReturnValue", "RedundantThrows", "ConstantConditions"})
%%

%class _TwigLexer
%implements FlexLexer
%final
%unicode
%function advance
%type IElementType

%{
    private Stack<Integer> stack = new Stack<Integer>();

    public void yypushState(int newState) {
      stack.push(yystate());
      yybegin(newState);
    }

    public void yypopState() {
      yybegin(stack.pop());
    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

// these will match the usual syntax and the whitespace controller ({{ and {{-, etc)
ExpressionOpen = \{\{-?
ExpressionClose = -?\}\}

StatementOpen = \{%-?
StatementClose = -?%\}

CommentOpen = \{#-?
CommentClose = -?#\}

Label = [A-z_][A-z0-9_]*

AnyChar = [.]
DoubleQuotesChars = (([^\"\\]|("\\"{AnyChar})))

%state expression
%state statement
%state statement_block_tag
%state comment
%%

<YYINITIAL> {
    {CommentOpen} {
        yybegin(comment);
        return TwigTokenTypes.COMMENT_OPEN;
    }

    {ExpressionOpen} {
        yybegin(expression);
        return TwigTokenTypes.EXPRESSION_OPEN;
    }

    {StatementOpen} {
        yybegin(statement_block_tag);
        return TwigTokenTypes.STATEMENT_OPEN;
    }

    !([^]*"{"[^]*) {
        return TwigTokenTypes.CONTENT;
    }
}

<comment> {
    {CommentClose} {
        yybegin(YYINITIAL);
        return TwigTokenTypes.COMMENT_CLOSE;
    }

    ~{CommentClose} {
        yypushback(2);
        return TwigTokenTypes.COMMENT_CONTENT;
    }

    {WhiteSpace} {}
}

<statement_block_tag> {
    {Label} {
        yybegin(statement);
        return TwigTokenTypes.TAG;
    }
    
    {StatementClose} {
        yybegin(YYINITIAL);
        return TwigTokenTypes.STATEMENT_CLOSE;
    }

    "," { }

    {WhiteSpace} {
        return TwigTokenTypes.WHITE_SPACE;
    }
}

<statement> {
    {StatementClose} {
        yybegin(YYINITIAL);
        return TwigTokenTypes.STATEMENT_CLOSE;
    }

    "starts with" |
    "ends with" |
    "matches" |
    "b-or" |
    "b-and" |
    "b-xor" |
    "and" |
    "not" |
    "in" |
    "is" |
    "if" |
    "or" {
        return TwigTokenTypes.OPERATOR;
    }

    "odd" |
    "even" {
        return TwigTokenTypes.TEST;
    }

    [\/.] { return TwigTokenTypes.SEP; }
//    [\t \n\x0B\f\r]* { return TwigTokenTypes.WHITE_SPACE; }
    \-?[0-9]+(\.[0-9]+)?/[}\)\t \n\x0B\f\r]  { return TwigTokenTypes.NUMBER; }
    "|" { return TwigTokenTypes.FILTER_SEP; }
    "=" { return TwigTokenTypes.EQUALS; }
}

<expression> {
    {ExpressionClose} {
        yybegin(YYINITIAL);
        return TwigTokenTypes.EXPRESSION_CLOSE;
    }
}

<statement, expression> {
    "(" {
        return TwigTokenTypes.OPEN_SEXPR;
    }

    ")" {
        return TwigTokenTypes.CLOSE_SEXPR;
    }

    "[" {
        return TwigTokenTypes.OPEN_LIST;
    }

    "]" {
        return TwigTokenTypes.CLOSE_LIST;
    }

    "{" {
        return TwigTokenTypes.OPEN_DICT;
    }

    "}" {
        return TwigTokenTypes.CLOSE_DICT;
    }

    "true"/[}\)\t \n\x0B\f\r] {
        return TwigTokenTypes.BOOLEAN;
    }

    "false"/[}\)\t \n\x0B\f\r] {
        return TwigTokenTypes.BOOLEAN;
    }

    {Label} {
        return TwigTokenTypes.VARIABLE;
    }

    {WhiteSpace} {
        return TwigTokenTypes.WHITE_SPACE;
    }
}

<statement, expression>(b?[\"]{DoubleQuotesChars}*[\"]) {
    return TwigTokenTypes.STRING;
}

<statement, expression>(b?[']([^'\\]|("\\"{AnyChar}))*[']) {
    return TwigTokenTypes.STRING;
}

<expression, statement, comment> {AnyChar} {
	// do nothing
}

{WhiteSpace}+ { return TwigTokenTypes.WHITE_SPACE; }
. { return TwigTokenTypes.INVALID; }
