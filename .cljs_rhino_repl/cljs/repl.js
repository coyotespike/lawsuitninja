// Compiled by ClojureScript 0.0-3308 {}
goog.provide('cljs.repl');
goog.require('cljs.core');
cljs.repl.print_doc = (function cljs$repl$print_doc(m){
cljs.core.println.call(null,"-------------------------");

cljs.core.println.call(null,[cljs.core.str((function (){var temp__4425__auto__ = new cljs.core.Keyword(null,"ns","ns",441598760).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(temp__4425__auto__)){
var ns = temp__4425__auto__;
return [cljs.core.str(ns),cljs.core.str("/")].join('');
} else {
return null;
}
})()),cljs.core.str(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join(''));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Protocol");
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m))){
var seq__7584_7596 = cljs.core.seq.call(null,new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m));
var chunk__7585_7597 = null;
var count__7586_7598 = (0);
var i__7587_7599 = (0);
while(true){
if((i__7587_7599 < count__7586_7598)){
var f_7600 = cljs.core._nth.call(null,chunk__7585_7597,i__7587_7599);
cljs.core.println.call(null,"  ",f_7600);

var G__7601 = seq__7584_7596;
var G__7602 = chunk__7585_7597;
var G__7603 = count__7586_7598;
var G__7604 = (i__7587_7599 + (1));
seq__7584_7596 = G__7601;
chunk__7585_7597 = G__7602;
count__7586_7598 = G__7603;
i__7587_7599 = G__7604;
continue;
} else {
var temp__4425__auto___7605 = cljs.core.seq.call(null,seq__7584_7596);
if(temp__4425__auto___7605){
var seq__7584_7606__$1 = temp__4425__auto___7605;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__7584_7606__$1)){
var c__5464__auto___7607 = cljs.core.chunk_first.call(null,seq__7584_7606__$1);
var G__7608 = cljs.core.chunk_rest.call(null,seq__7584_7606__$1);
var G__7609 = c__5464__auto___7607;
var G__7610 = cljs.core.count.call(null,c__5464__auto___7607);
var G__7611 = (0);
seq__7584_7596 = G__7608;
chunk__7585_7597 = G__7609;
count__7586_7598 = G__7610;
i__7587_7599 = G__7611;
continue;
} else {
var f_7612 = cljs.core.first.call(null,seq__7584_7606__$1);
cljs.core.println.call(null,"  ",f_7612);

var G__7613 = cljs.core.next.call(null,seq__7584_7606__$1);
var G__7614 = null;
var G__7615 = (0);
var G__7616 = (0);
seq__7584_7596 = G__7613;
chunk__7585_7597 = G__7614;
count__7586_7598 = G__7615;
i__7587_7599 = G__7616;
continue;
}
} else {
}
}
break;
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m))){
var arglists_7617 = new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_((function (){var or__4679__auto__ = new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__4679__auto__)){
return or__4679__auto__;
} else {
return new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m);
}
})())){
cljs.core.prn.call(null,arglists_7617);
} else {
cljs.core.prn.call(null,((cljs.core._EQ_.call(null,new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.first.call(null,arglists_7617)))?cljs.core.second.call(null,arglists_7617):arglists_7617));
}
} else {
}
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"special-form","special-form",-1326536374).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Special Form");

cljs.core.println.call(null," ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m));

if(cljs.core.contains_QMARK_.call(null,m,new cljs.core.Keyword(null,"url","url",276297046))){
if(cljs.core.truth_(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))){
return cljs.core.println.call(null,[cljs.core.str("\n  Please see http://clojure.org/"),cljs.core.str(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))].join(''));
} else {
return null;
}
} else {
return cljs.core.println.call(null,[cljs.core.str("\n  Please see http://clojure.org/special_forms#"),cljs.core.str(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join(''));
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Macro");
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"REPL Special Function");
} else {
}

cljs.core.println.call(null," ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
var seq__7588 = cljs.core.seq.call(null,new cljs.core.Keyword(null,"methods","methods",453930866).cljs$core$IFn$_invoke$arity$1(m));
var chunk__7589 = null;
var count__7590 = (0);
var i__7591 = (0);
while(true){
if((i__7591 < count__7590)){
var vec__7592 = cljs.core._nth.call(null,chunk__7589,i__7591);
var name = cljs.core.nth.call(null,vec__7592,(0),null);
var map__7593 = cljs.core.nth.call(null,vec__7592,(1),null);
var map__7593__$1 = ((cljs.core.seq_QMARK_.call(null,map__7593))?cljs.core.apply.call(null,cljs.core.hash_map,map__7593):map__7593);
var doc = cljs.core.get.call(null,map__7593__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists = cljs.core.get.call(null,map__7593__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println.call(null);

cljs.core.println.call(null," ",name);

cljs.core.println.call(null," ",arglists);

if(cljs.core.truth_(doc)){
cljs.core.println.call(null," ",doc);
} else {
}

var G__7618 = seq__7588;
var G__7619 = chunk__7589;
var G__7620 = count__7590;
var G__7621 = (i__7591 + (1));
seq__7588 = G__7618;
chunk__7589 = G__7619;
count__7590 = G__7620;
i__7591 = G__7621;
continue;
} else {
var temp__4425__auto__ = cljs.core.seq.call(null,seq__7588);
if(temp__4425__auto__){
var seq__7588__$1 = temp__4425__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__7588__$1)){
var c__5464__auto__ = cljs.core.chunk_first.call(null,seq__7588__$1);
var G__7622 = cljs.core.chunk_rest.call(null,seq__7588__$1);
var G__7623 = c__5464__auto__;
var G__7624 = cljs.core.count.call(null,c__5464__auto__);
var G__7625 = (0);
seq__7588 = G__7622;
chunk__7589 = G__7623;
count__7590 = G__7624;
i__7591 = G__7625;
continue;
} else {
var vec__7594 = cljs.core.first.call(null,seq__7588__$1);
var name = cljs.core.nth.call(null,vec__7594,(0),null);
var map__7595 = cljs.core.nth.call(null,vec__7594,(1),null);
var map__7595__$1 = ((cljs.core.seq_QMARK_.call(null,map__7595))?cljs.core.apply.call(null,cljs.core.hash_map,map__7595):map__7595);
var doc = cljs.core.get.call(null,map__7595__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists = cljs.core.get.call(null,map__7595__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println.call(null);

cljs.core.println.call(null," ",name);

cljs.core.println.call(null," ",arglists);

if(cljs.core.truth_(doc)){
cljs.core.println.call(null," ",doc);
} else {
}

var G__7626 = cljs.core.next.call(null,seq__7588__$1);
var G__7627 = null;
var G__7628 = (0);
var G__7629 = (0);
seq__7588 = G__7626;
chunk__7589 = G__7627;
count__7590 = G__7628;
i__7591 = G__7629;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
}
});

//# sourceMappingURL=repl.js.map