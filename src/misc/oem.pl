#!/usr/bin/perl

use strict;
use warnings;

for (0x30..0x3f) {
	my $x = uc sprintf("%2.2x", $_);
	print "OEM_$x(0x$x),\n";
}
