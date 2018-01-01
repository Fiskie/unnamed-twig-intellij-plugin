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
WhiteSpace = {LineTerminator} | [ \t\x0B\f]

// these will match the usual syntax and the whitespace controller ({{ and {{-, etc)
ExpressionOpen = \{\{-?
ExpressionClose = -?\}\}

StatementOpen = \{%-?
StatementClose = -?%\}

CommentOpen = \{#-?
CommentClose = -?#\}

Label = [A-Za-z_]\w*

AnyChar = [.]
DoubleQuotesChars = (([^\"\\]|("\\"{AnyChar})))

%state expression_block
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
            yybegin(expression_block);
            return TwigTokenTypes.EXPRESSION_OPEN;
        }

    {StatementOpen} {
        yybegin(statement_block_tag);
        return TwigTokenTypes.STATEMENT_OPEN;
    }

    !([^]*"{"[^]*) {
        if (!yytext().toString().equals("")) {
            if (yytext().toString().trim().length() == 0) {
                return TwigTokenTypes.WHITE_SPACE;
            } else {
                return TwigTokenTypes.CONTENT;
            }
        }
    }
}

<comment> {
    {CommentClose} {
        yybegin(YYINITIAL);
        return TwigTokenTypes.COMMENT_CLOSE;
    }

    ~{CommentClose} {
        // handle the extra - if it's a whitespace controlling tag
        if (yytext().subSequence(yylength() - 3, yylength() - 2).toString().equals("-")) {
            yypushback(1);
        }

        yypushback(2);
        return TwigTokenTypes.COMMENT_CONTENT;
    }

    // lex unclosed comments so that we can give better errors
    !([^]*"#}"[^]*) { return TwigTokenTypes.UNCLOSED_COMMENT; }
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

    {WhiteSpace} { return TwigTokenTypes.WHITE_SPACE; }
}

<statement> {
    {StatementClose} {
        yybegin(YYINITIAL);
        return TwigTokenTypes.STATEMENT_CLOSE;
    }

    "odd" |
    "even" {
        return TwigTokenTypes.TEST;
    }

//    [\t \n\x0B\f\r]* { return TwigTokenTypes.WHITE_SPACE; }
    "=" { return TwigTokenTypes.EQUALS; }
}

<expression_block> {
    {ExpressionClose} {
        yybegin(YYINITIAL);
        return TwigTokenTypes.EXPRESSION_CLOSE;
    }
}

<statement, expression_block> {
    "(" { return TwigTokenTypes.LPARENTH; }
    ")" { return TwigTokenTypes.RPARENTH; }
    "[" { return TwigTokenTypes.LBRACKET; }
    "]" { return TwigTokenTypes.RBRACKET; }
    "{" { return TwigTokenTypes.LBRACE; }
    "}" { return TwigTokenTypes.RBRACE; }
    "true"/[}\)\t \n\x0B\f\r] { return TwigTokenTypes.BOOLEAN; }
    "false"/[}\)\t \n\x0B\f\r] { return TwigTokenTypes.BOOLEAN; }
    \-?[0-9]+(\.[0-9]+)?/[}\)\t \n\x0B\f\r] { return TwigTokenTypes.NUMBER; }
    "|" { return TwigTokenTypes.FILTER_SEP; }
    [\/.] { return TwigTokenTypes.SEP; }

    // in the order of operator precedence (except for 'not', which is a negator)
    "not" |
    "b-and" | "b-xor" | "b-or" |
    "or" | "and" |
    "==" | "!=" | "<" | ">" | ">=" | "<=" |
    "in" |
    "matches" |
    "starts with" | "ends with" |
    ".." |
    "+" | "-" | "~" | "*" | "/" | "//" | "%" | "is" | "**" |
    "??" | "?:" {
        return TwigTokenTypes.OPERATOR;
    }

    "," { return TwigTokenTypes.COMMA; }

    {Label} { return TwigTokenTypes.VARIABLE; }

    {WhiteSpace} { return TwigTokenTypes.WHITE_SPACE; }
}

<statement, expression_block>(b?[\"]{DoubleQuotesChars}*[\"]) {
    return TwigTokenTypes.STRING;
}

<statement, expression_block>(b?[']([^'\\]|("\\"{AnyChar}))*[']) {
    return TwigTokenTypes.STRING;
}

<expression_block, statement, comment> {AnyChar} {
	// do nothing
}

{WhiteSpace}+ { return TwigTokenTypes.WHITE_SPACE; }
. { return TwigTokenTypes.INVALID; }
