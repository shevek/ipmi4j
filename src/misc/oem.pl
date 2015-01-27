#!/usr/bin/perl

use strict;
use warnings;

for (0xC0..0xFF) {
	my $x = uc sprintf("%2.2x", $_);
	print "OEM_Reserved_$x(0x$x, \"OEM Reserved ID 0x$x\"),\n";
}
