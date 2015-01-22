#!/usr/bin/perl

use strict;
use warnings;

for (0xC0..0xFf) {
	my $x = uc sprintf("%2.2x", $_);
	print "OEM_$x(0x$x, \"OEM type 0x$x\"),\n";
}
