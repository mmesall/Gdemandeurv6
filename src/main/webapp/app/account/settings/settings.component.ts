import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
})
export class SettingsComponent implements OnInit {
  account!: Account;
  success = false;
  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(50), Validators.email]],
    dateNaiss: [undefined, [Validators.required]],
    lieuNaiss: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(50)]],
    regionNaiss: [undefined, [Validators.required]],
    typePiece: [undefined, [Validators.required]],
    numeroPiece: [undefined, [Validators.required, Validators.minLength(13), Validators.maxLength(14)]],
    sexe: [undefined, [Validators.required]],
    regionResidence: [undefined, [Validators.required]],
    adresseResidence: [undefined, [Validators.required]],
    telephone1: [undefined, [Validators.required, Validators.minLength(9), Validators.maxLength(14)]],
    telephone2: [undefined, [Validators.required, Validators.minLength(9), Validators.maxLength(14)]],
    nomFormation: [undefined, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    dateDebut: [undefined, [Validators.required]],
    dateFin: [undefined, [Validators.required]],
    etab_freq: [undefined, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    niveauFormation: [undefined, [Validators.required]],
    specialite1: [undefined, [Validators.required]],
    status: [undefined, [Validators.required]],
    perspective: [undefined, [Validators.required]],
    structure: [undefined, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    profession: [undefined, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    secteur: [undefined, [Validators.required]],
    langue1: [undefined, [Validators.required]],
    langue2: [undefined, [Validators.required]],
  });
  dateNaissDp: any;
  nomRegionValues: any;
  niveauEtudeValues: any;
  editForm: any;

  constructor(private accountService: AccountService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.settingsForm.patchValue({
          firstName: account.firstName,
          lastName: account.lastName,
          email: account.email,
        });

        this.account = account;
      }
    });
  }

  save(): void {
    this.success = false;

    this.account.firstName = this.settingsForm.get('firstName')!.value;
    this.account.lastName = this.settingsForm.get('lastName')!.value;
    this.account.email = this.settingsForm.get('email')!.value;

    this.accountService.save(this.account).subscribe(() => {
      this.success = true;

      this.accountService.authenticate(this.account);
    });
  }
}
