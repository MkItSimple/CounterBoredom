package com.mkitsimple.counterboredom.di

import com.mkitsimple.counterboredom.di.modules.AppModule
import com.mkitsimple.counterboredom.di.scopes.AppScope
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {
    //fun newPokemonLisComponent(): PokemonListComponent
    //fun newPokemonDetailsComponent(): PokemonDetailsComponent
}