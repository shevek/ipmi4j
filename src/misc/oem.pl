#!/usr/bin/perl

use strict;
use warnings;

for (0x70..0x7F) {
	my $x = uc sprintf("%2.2x", $_);
	print "OEM_$x(0x$x, GenericEventCategory.OEM, \"OEM Reading Type 0x$x\"),\n";
}
