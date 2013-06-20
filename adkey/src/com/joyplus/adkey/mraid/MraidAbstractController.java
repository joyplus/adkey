package com.joyplus.adkey.mraid;


class MraidAbstractController {
    private final MraidView mView;

    MraidAbstractController(MraidView view) {
        super();
        mView = view;
    }
    
    public MraidView getView() {
        return mView;
    }
}