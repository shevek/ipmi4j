#!/usr/bin/perl

use strict;
use warnings;

for (0x00..0x7F) {
	my $x = uc sprintf("%2.2x", $_);
	print "I2CAddresss$x(0x$x),\n";
}
