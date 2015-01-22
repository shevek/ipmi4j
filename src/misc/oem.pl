#!/usr/bin/perl

use strict;
use warnings;

for (0xD0..0xFF) {
	my $x = uc sprintf("%2.2x", $_);
	print "OEM_Specific_$x(0x$x, \"OEM Specific ID 0x$x\"),\n";
}
