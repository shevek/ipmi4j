#!/usr/bin/perl

use strict;
use warnings;

for (0x20..0x5f) {
	my $x = uc sprintf("%2.2x", $_);
	my $i = $_ - 0x20;
	print "SystemInterrupt$i(0x$x),\n";
}
