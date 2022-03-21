package my.logon.screen.listeners;


import my.logon.screen.enums.EnumMotiveSuspendareObKA;
import my.logon.screen.enums.EnumStadiuObiectivKA;
import my.logon.screen.enums.EnumStadiuSubantrep;

public interface StadiuDialogListener {
	void stadiuSelected(EnumStadiuSubantrep stadiuObiectiv);
	void stadiuSelected(EnumStadiuObiectivKA stadiuObiectiv, EnumMotiveSuspendareObKA motivSuspendare);

}
