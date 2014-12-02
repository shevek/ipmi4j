#!/usr/bin/perl

use strict;
use warnings;

my %netfn = (
	App	=> 'IpmiNetworkFunction.App',
	Transport	=> 'IpmiNetworkFunction.Transport',
	Bridge	=> 'IpmiNetworkFunction.Bridge',
	'S/E'	=> 'IpmiNetworkFunction.Sensor',
	Storage	=> 'IpmiNetworkFunction.Storage',
	Chassis	=> 'IpmiNetworkFunction.Chassis',
);

while (<>) {
	next if /^$/;
	print and next if m,^//,;
	my @words = split;
	my $code = pop @words; 
	$code =~ s/h$//;
	my $netfn = pop @words;
	$netfn = $netfn{$netfn} || $netfn;
	my $section = pop @words;
	my $command = join('', @words);
	print "$command(\"@words\", $netfn, 0x$code),\n";
}
