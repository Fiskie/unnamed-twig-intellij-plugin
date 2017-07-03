package com.fisk.twig.parsing

import com.intellij.lexer.FlexAdapter

import java.io.Reader


class TwigRawLexer : FlexAdapter(_TwigLexer(null as Reader?))